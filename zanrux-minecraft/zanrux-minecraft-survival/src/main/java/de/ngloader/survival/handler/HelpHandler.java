package de.ngloader.survival.handler;

import org.bukkit.inventory.ItemStack;

import de.ngloader.core.help.HelpCategory;
import de.ngloader.core.help.HelpSystem;
import de.ngloader.core.help.IHelp;
import de.ngloader.core.util.ItemFactory;
import de.ngloader.survival.Survival;
import de.ngloader.survival.config.ConfigHelp;
import de.ngloader.survival.config.ConfigHelp.Category;
import de.ngloader.survival.config.ConfigHelp.Element;
import de.ngloader.synced.IHandler;
import de.ngloader.synced.config.ConfigService;

public class HelpHandler extends IHandler<Survival> {

	public HelpHandler(Survival core) {
		super(core);
	}

	@Override
	public void onEnable() {
		ConfigHelp config = ConfigService.getConfig(ConfigHelp.class);
		HelpSystem helpSystem = this.core.getHelpSystem();

		for (Category category : config.categorys) {
			try {
				HelpCategory helpCategory = new HelpCategory(new ItemFactory(category.material)
						.setDisplayName(category.getDisplayName())
						.setLore(category.getLore())
						.addAllFlag());
				helpSystem.addCategory(helpCategory);

				for (Element element : category.elements) {
					try {
						helpSystem.addHelp(helpCategory, new IHelp() {
							
							@Override
							public String getName() {
								return element.name;
							}
							
							@Override
							public ItemStack getDisplayItem() {
								return new ItemFactory(element.material)
										.setDisplayName(element.getDisplayName())
										.setLore(element.getLore())
										.addAllFlag()
										.build();
							}
							
							@Override
							public String getDescription() {
								return element.getDescription();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDisable() {
		this.core.getHelpSystem().clear();
	}
}