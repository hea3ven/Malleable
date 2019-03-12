#! /usr/bin/env python3
"""Generate data"""

import sys
import os.path
import PIL.Image
import PIL.ImageChops


class Metal:
    def __init__(self, name, tint, supports_tools=True, ore_background=None):
        self.name = name
        self.tint = tint
        self.supports_tools = supports_tools
        self.ore_background = ore_background

    def is_ore(self):
        return self.ore_background is not None


DATA = [
        Metal('tin', (210, 225, 225), supports_tools=False, ore_background='stone'),
        Metal('copper', (255, 195, 100), supports_tools=False, ore_background='stone'),
        Metal('bronze', (225, 170, 80)),
        Metal('steel', (100, 100, 110)),
        Metal('cobalt', (85, 100, 185), ore_background='netherrack'),
        Metal('fercosteel', (50, 60, 105)),
        Metal('tungsten', (60, 50, 50), ore_background='end_stone'),
        Metal('mushetsteel', (25, 35, 25)),
]


class TemplateEngine:
    def __init__(self, tpl_dir, clean):
        self.tpl_dir = tpl_dir
        self.clean = clean

    def render(self, name, output, *args, **kwargs):
        if self.clean:
            os.remove(output)
            return
        with open(os.path.join(self.tpl_dir, name + '.json')) as tpl_file:
            tpl = tpl_file.read()
        with open(output, 'w') as output_file:
            try:
                if args:
                    output_file.write(tpl % args)
                else:
                    output_file.write(tpl % kwargs)
            except Exception as e:
                print("Error rendering %s %% (%s, %s): %s" % (output, args, kwargs, e))

    def render_image(self, name, output, tint, background=None):
        images = [(name, tint)]
        if background is not None:
            images.append((background, None))
        self.render_images(images, output)

    def render_images(self, images, output):
        if self.clean:
            os.remove(output)
            return
        result = None
        for src_image, tint in images:
            image = PIL.Image.open(os.path.join(self.tpl_dir, src_image + '.png'))
            if tint is not None:
                overlay_image = PIL.Image.new("RGBA", (image.width, image.height), tint)
                image = PIL.ImageChops.multiply(overlay_image, image)
            if result is None:
                result = image
            else:
                result = PIL.ImageChops.composite(result, image, result)
        result.save(output)


def main(args):
    AssetsRenderer(os.path.dirname(os.path.realpath(__file__)), 'clean' in args).render()


class AssetsRenderer:
    def __init__(self, base_dir, clean):
        self.res_dir = os.path.join(base_dir, 'src/main/resources')
        self.tpl = TemplateEngine(os.path.join(base_dir, 'src/main/templates'), clean)

    def get_output_dir(self, dirname, filename):
        return os.path.join(self.res_dir, dirname, filename)

    def get_blockstate_file(self, name, *args):
        return self.get_output_dir('assets/malleable/blockstates', name.format(*args))

    def get_block_model_file(self, name, *args):
        return self.get_output_dir('assets/malleable/models/block', name.format(*args))

    def get_item_model_file(self, name, *args):
        return self.get_output_dir('assets/malleable/models/item', name.format(*args))

    def get_block_texture_file(self, name, *args):
        return self.get_output_dir('assets/malleable/textures/blocks', name.format(*args))

    def get_item_texture_file(self, name, *args):
        return self.get_output_dir('assets/malleable/textures/items', name.format(*args))

    def get_armor_texture_file(self, name, *args):
        return self.get_output_dir('assets/minecraft/textures/models/armor', name.format(*args))

    def get_block_loot_table_file(self, name, *args):
        return self.get_output_dir('data/malleable/loot_tables/blocks', name.format(*args))

    def get_recipe_file(self, name, *args):
        return self.get_output_dir('data/malleable/recipes', name.format(*args))

    def render(self):
        for metal in DATA:
            if metal.is_ore():
                self.render_ore(metal)

            self.render_block(metal)
            self.render_ingot(metal)
            self.render_nugget(metal)

            if metal.supports_tools:
                self.render_pickaxe(metal)
                self.render_axe(metal)
                self.render_sword(metal)
                self.render_shovel(metal)
                self.render_hoe(metal)

                self.render_armor(metal)
                self.render_helmet(metal)
                self.render_chestplate(metal)
                self.render_leggings(metal)
                self.render_boots(metal)
        self.render_bronze_mix()
        self.render_steel_mix()
        self.render_fercosteel_mix()
        self.render_mushetsteel_mix()

        self.render_disabled_recipes()

    def render_ore(self, metal):
        self.tpl.render('ore_blockstate', self.get_blockstate_file('{}_ore.json', metal.name), metal.name)
        self.tpl.render('ore_model_block', self.get_block_model_file('{}_ore.json', metal.name), metal.name)
        self.tpl.render('ore_model_item', self.get_item_model_file('{}_ore.json', metal.name), metal.name)

        self.tpl.render_image('ore_overlay', self.get_block_texture_file('{}_ore.png', metal.name), metal.tint,
                              background=metal.ore_background)

        self.tpl.render('ore_smelting_recipe', self.get_recipe_file('{}_ingot.json', metal.name),
                        '{}_ore'.format(metal.name), metal.name)
        self.tpl.render('ore_blasting_recipe', self.get_recipe_file('{}_ingot_from_blasting.json', metal.name),
                        '{}_ore'.format(metal.name), metal.name)
        self.render_loot_table('{}_ore'.format(metal.name))

    def render_block(self, metal):
        self.tpl.render('block_blockstate', self.get_blockstate_file('{}_block.json', metal.name), metal.name)
        self.tpl.render('block_model_block', self.get_block_model_file('{}_block.json', metal.name), metal.name)
        self.tpl.render('block_model_item', self.get_item_model_file('{}_block.json', metal.name), metal.name)
        self.tpl.render_image('block', self.get_block_texture_file('{}_block.png', metal.name), metal.tint)
        self.tpl.render('block_to_ingot_recipe',
                        self.get_recipe_file('{}_ingot_from_{}_block.json', metal.name, metal.name),
                        metal.name,
                        metal.name,
                        metal.name)
        self.tpl.render('nugget_to_ingot_recipe',
                        self.get_recipe_file('{}_ingot_from_{}_nugget.json', metal.name, metal.name),
                        metal.name,
                        metal.name,
                        metal.name)
        self.tpl.render('ingot_to_nugget_recipe',
                        self.get_recipe_file('{}_nugget_from_{}_ingot.json', metal.name, metal.name),
                        metal.name,
                        metal.name)
        self.tpl.render('block_recipe', self.get_recipe_file('{}_block.json', metal.name), metal.name,
                        metal.name)
        self.render_loot_table('{}_block'.format(metal.name))

    def render_ingot(self, metal):
        self.render_item(metal, 'ingot')

    def render_nugget(self, metal):
        self.render_item(metal, 'nugget')

    def render_pickaxe(self, metal):
        self.render_item_handheld(metal, 'pickaxe', tool_background='pickaxe_handle')
        self.render_shaped_recipe('{}_ingot'.format(metal.name), '{}_pickaxe'.format(metal.name), 'XXX', ' # ', ' # ')

    def render_axe(self, metal):
        self.render_item_handheld(metal, 'axe', tool_background='axe_handle')
        self.render_shaped_recipe('{}_ingot'.format(metal.name), '{}_axe'.format(metal.name), 'XX ', 'X# ', ' # ')

    def render_sword(self, metal):
        self.render_item_handheld(metal, 'sword', tool_background='sword_handle')
        self.render_shaped_recipe('{}_ingot'.format(metal.name), '{}_sword'.format(metal.name), ' X ', ' X ', ' # ')

    def render_shovel(self, metal):
        self.render_item_handheld(metal, 'shovel', tool_background='shovel_handle')
        self.render_shaped_recipe('{}_ingot'.format(metal.name), '{}_shovel'.format(metal.name), ' X ', ' # ', ' # ')

    def render_hoe(self, metal):
        self.render_item_handheld(metal, 'hoe', tool_background='hoe_handle')
        self.render_shaped_recipe('{}_ingot'.format(metal.name), '{}_hoe'.format(metal.name), 'XX ', ' # ', ' # ')

    def render_armor(self, metal):
        self.tpl.render_image('armor_layer_1', self.get_armor_texture_file('{}_layer_1.png', metal.name), metal.tint)
        self.tpl.render_image('armor_layer_2', self.get_armor_texture_file('{}_layer_2.png', metal.name), metal.tint)

    def render_helmet(self, metal):
        self.render_item_generated(metal, "helmet")
        self.render_armor_recipe(metal, "helmet", "###", "# #")

    def render_chestplate(self, metal):
        self.render_item_generated(metal, "chestplate")
        self.render_armor_recipe(metal, "helmet", "# #", "###", "###")

    def render_leggings(self, metal):
        self.render_item_generated(metal, "leggings")
        self.render_armor_recipe(metal, "helmet", "###", "# #", "# #")

    def render_boots(self, metal):
        self.render_item_generated(metal, "boots")
        self.render_armor_recipe(metal, "helmet", "###", "# #")

    def render_item(self, metal, item_name, tool_background=None):
        self.tpl.render('{}_model_item'.format(item_name),
                        self.get_item_model_file('{}_{}.json', metal.name, item_name),
                        metal.name)
        self.tpl.render_image(item_name, self.get_item_texture_file('{}_{}.png', metal.name, item_name), metal.tint,
                              background=tool_background)

    def render_item_handheld(self, metal, item_name, tool_background=None):
        self.tpl.render('handheld_model_item', self.get_item_model_file('{}_{}.json', metal.name, item_name),
                        metal.name, item_name)
        self.tpl.render_image(item_name, self.get_item_texture_file('{}_{}.png', metal.name, item_name), metal.tint,
                              background=tool_background)

    def render_item_generated(self, metal, item_name):
        self.tpl.render('generated_model_item', self.get_item_model_file('{}_{}.json', metal.name, item_name),
                        '{}_{}'.format(metal.name, item_name))
        self.tpl.render_image(item_name, self.get_item_texture_file('{}_{}.png', metal.name, item_name), metal.tint)

    def render_bronze_mix(self):
        self.tpl.render_images([('alloy_mix_3_1__3', DATA[1].tint), ('alloy_mix_3_1__1', DATA[0].tint)],
                               self.get_item_texture_file('bronze_mix.png'))
        self.render_alloy(DATA[2])

    def render_steel_mix(self):
        self.tpl.render_images([('alloy_mix_2_c__2', (200, 230, 240)), ('alloy_mix_2_c__c', None)],
                               self.get_item_texture_file('steel_mix.png'))
        self.render_alloy(DATA[3])

    def render_fercosteel_mix(self):
        self.tpl.render_images([('alloy_mix_3_1__1', DATA[3].tint), ('alloy_mix_3_1__3', DATA[4].tint)],
                               self.get_item_texture_file('fercosteel_mix.png'))
        self.render_alloy(DATA[5])

    def render_mushetsteel_mix(self):
        self.tpl.render_images([('alloy_mix_2_1__2', DATA[3].tint), ('alloy_mix_2_1__1', DATA[6].tint)],
                               self.get_item_texture_file('mushetsteel_mix.png'))
        self.render_alloy(DATA[7])

    def render_alloy(self, metal):
        self.tpl.render('ore_smelting_recipe', self.get_recipe_file('{}_nugget.json'.format(metal.name)),
                        '{}_mix'.format(metal.name), metal.name)
        self.tpl.render('ore_blasting_recipe', self.get_recipe_file('{}_nugget_from_blasting.json'.format(metal.name)),
                        '{}_mix'.format(metal.name), metal.name)
        self.tpl.render('generated_model_item', self.get_item_model_file('{}_mix.json', metal.name),
                        '{}_mix'.format(metal.name))

    def render_disabled_recipes(self):
        self.render_disabled_recipe("diamond_pickaxe")
        self.render_disabled_recipe("diamond_axe")
        self.render_disabled_recipe("diamond_sword")
        self.render_disabled_recipe("diamond_hoe")
        self.render_disabled_recipe("diamond_shovel")
        self.render_disabled_recipe("diamond_helmet")
        self.render_disabled_recipe("diamond_chestplate")
        self.render_disabled_recipe("diamond_leggings")
        self.render_disabled_recipe("diamond_boots")

    def render_disabled_recipe(self, name):
        self.tpl.render('empty_recipe', self.get_output_dir('data/minecraft/recipes', name + ".json"), name)

    def render_shaped_recipe(self, ingredient, output, recipe_line1, recipe_line2, recipe_line3):
        self.tpl.render('shaped_recipe', self.get_recipe_file(output + ".json"), recipe_line1, recipe_line2,
                        recipe_line3, ingredient, output)

    def render_armor_recipe(self, metal, armor_part, recipe_line1, recipe_line2, recipe_line3=None):
        if recipe_line3 is None:
            self.tpl.render('shaped_armor_recipe2', self.get_recipe_file("{}_{}.json".format(metal.name, armor_part)),
                            recipe_line1, recipe_line2, metal.name, metal.name, armor_part)
        else:
            self.tpl.render('shaped_armor_recipe', self.get_recipe_file("{}_{}.json".format(metal.name, armor_part)),
                            recipe_line1, recipe_line2, recipe_line3, metal.name, metal.name, armor_part)

    def render_loot_table(self, block_name):
        self.tpl.render('block_loot_table', self.get_block_loot_table_file('{}.json', block_name),
                        block_name)


if __name__ == '__main__':
    main(sys.argv)
