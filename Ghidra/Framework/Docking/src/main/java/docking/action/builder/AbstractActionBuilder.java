/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package docking.action.builder;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.Icon;

import docking.*;
import docking.action.*;
import ghidra.util.HelpLocation;
import resources.ResourceManager;

/**
 * Base class for DockingAction builders
 *
 * @param <T> The type of DockingAction to build
 * @param <B> the Type of DockingActionBuilder
 */
public abstract class AbstractActionBuilder<T extends DockingActionIf, B extends AbstractActionBuilder<T, B>> {
	/**
	 * a name for the {@code DockingAction}
	 */
	protected String name;
	/**
	 * an owner for the {@code DockingAction}
	 */
	protected String owner;
	/**
	 * the {@code KeyBindingType} for this {@code DockingAction}
	 */
	protected KeyBindingType keyBindingType = KeyBindingType.INDIVIDUAL;
	/**
	 * The callback to perform when the action is invoked
	 */
	protected Consumer<ActionContext> actionCallback;
	/**
	 * a description for the {@code DockingAction}
	 */
	private String description = "";
	/**
	 * whether this {@code DockingAction} is enabled
	 */
	private boolean isEnabled = true;
	/**
	 * the {@code HelpLocation} for this {@code DockingAction}
	 */
	private HelpLocation helpLocation;

	/**
	 * The menu bar path.  This is the key attribute for including the action on the menu bar
	 */
	private String[] menuPath;
	/**
	 * The menu bar menu item icon.  (optional)
	 */
	private Icon menuIcon;
	/**
	 * The menu bar menu item sub group.  (optional)
	 */
	private String menuSubGroup;
	/**
	 * The menu bar menu item group.  (optional)
	 */
	private String menuGroup;
	/**
	 * The mnemonic for the menu action (optional)
	 */
	private int menuMnemonic;
	/**
	 * The icon for the  menu item (optional)
	 */
	private Icon popupIcon;
	/**
	 * The menu path in a pop-up menu.  This is the key attribute for pop-up menu actions
	 */
	private String[] popupPath;
	/**
	 * The menu group for the item in the pop-up menu (optional)
	 */
	private String popupGroup;
	/**
	 * The menu sub group for the item in the pop-up menu (optional)
	 */
	private String popupSubGroup;
	/**
	 * The icon for the tool bar action.  This is the key attribute for actions in the toolbar.
	 */
	private Icon toolbarIcon;
	/**
	 * The group for the items on the tool bar (optional)
	 */
	private String toolBarGroup;
	/**
	 * The menu group for the item in the tool bar menu (optional)
	 */
	private String toolBarSubGroup;

	/**
	 * Predicate for determining if an action is enabled for a given context
	 */
	private Predicate<ActionContext> enabledPredicate;
	/**
	 * Predicate for determining if an action should be included on the pop-up menu
	 */
	private Predicate<ActionContext> popupPredicate;
	/**
	 * Predicate for determining if an action is applicable for a given context
	 */
	private Predicate<ActionContext> validContextPredicate;
	/**
	 * Predicate for determining if an action is applicable for a given global context
	 */
	private Predicate<ActionContext> validGlobalContextPredicate;


	/**
	 * Builder constructor
	 * @param name the name of the action to be built
	 * @param owner the owner of the action to be build
	 */
	public AbstractActionBuilder(String name, String owner) {
		this.name = name;
		this.owner = owner;
	}

	/**
	 * returns this (typed for subclass) for chaining
	 * @return this for chaining
	 */
	protected abstract B self();

	/**
	 * builds the action
	 * @return the newly build action 
	 */
	public abstract T build();

	/**
	 * Builds and adds the action globally to the tool
	 * 
	 * @param tool the tool to add the action to
	 * @return the newly created action
	 */
	public T buildAndInstall(DockingTool tool) {
		T action = build();
		tool.addAction(action);
		return action;
	}

	/**
	 * Builds and adds the action as a local action for the given provider
	 * 
	 * @param provider the provider to add the action to
	 * @return the newly created action
	 */
	public T buildAndInstallLocal(ComponentProvider provider) {
		T action = build();
		provider.addLocalAction(action);
		return action;
	}

	/**
	 * Configure the {@code String} description for this {@code DockingAction}
	 * 
	 * @param text the description
	 * @return this builder (for chaining)
	 */
	public B description(String text) {
		this.description = text;
		return self();
	}

	/**
	 * Configure whether this {@code DockingAction} is enabled
	 * 
	 * @param b {@code true} if enabled
	 * @return this builder (for chaining)
	 */
	public B enabled(boolean b) {
		this.isEnabled = b;
		return self();
	}

	/**
	 * Configure {@link KeyBindingType} for this {@code DockingAction}
	 * @param type the {@link KeyBindingType} to configure
	 * @return this builder (for chaining)
	 */
	public B keyBindingType(KeyBindingType type) {
		this.keyBindingType = type;
		return self();
	}
	
	/**
	 * Configure {@link HelpLocation} for this {@code DockingAction}
	 * @param help the {@link HelpLocation} to configure
	 * @return this builder (for chaining)
	 */
	public B helpLocation(HelpLocation help) {
		this.helpLocation = help;
		return self();
	}

	/**
	 * Sets the menu bar path for the action.  Setting this attribute is what causes the action
	 * to appear on the tools menu bar  
	 * @param pathElement the menu bar path for the action
	 * @return this builder (for chaining)
	 */
	public B menuPath(String... pathElement) {
		menuPath = pathElement;
		return self();
	}

	/**
	 * Sets the group for the action in the menu bar.  Actions in the same group will appear
	 * next to other actions in the same group and actions in different groups will be separated
	 * by menu dividers
	 * 
	 * @param group for this action
	 * @return this builder (for chaining)
	 */
	public B menuGroup(String group) {
		menuGroup = group;
		return self();
	}

	/**
	 * Sets the group and sub-group for the action in the menu bar.  Actions in the same group will appear
	 * next to other actions in the same group and actions in different groups will be separated
	 * by menu dividers.  The sub-group is used to order the actions withing the group.
	 * 
	 * @param group the group used to clump actions together.
	 * @param subGroup the sub-group used to order actions within a group.
	 * @return this builder (for chaining)
	 */
	public B menuGroup(String group, String subGroup) {
		menuSubGroup = group;
		return self();
	}

	/**
	 * Sets the icon to use in this action's menu bar item.
	 * 
	 * @param icon the icon to use in the action's menu bar item. 
	 * @return this builder (for chaining)
	 */
	public B menuIcon(Icon icon) {
		menuIcon = icon;
		return self();
	}

	/**
	 * Sets the mnemonic to use in this action's menu bar item.
	 * 
	 * @param mnemonic the mnemonic to use for this action's menu bar item. 
	 * @return this builder (for chaining)
	 */
	public B menuMnemonic(int mnemonic) {
		menuMnemonic = mnemonic;
		return self();
	}

	/**
	 * Sets the pop-up menu path for the action.  Setting this attribute is what causes the action
	 * to appear on the tools pop-up menu (Assuming it is applicable for the context)
	 * 
	 * @param pathElement the menu path for the action in the pop-up menu
	 * @return this builder (for chaining)
	 */
	public B popupMenuPath(String... pathElement) {
		popupPath = pathElement;
		return self();
	}

	/**
	 * Sets the group for the action in the pop-up menu.  Actions in the same group will appear
	 * next to other actions in the same group and actions in different groups will be separated
	 * by menu dividers
	 * 
	 * @param group for this action
	 * @return this builder (for chaining)
	 */
	public B popupMenuGroup(String group) {
		popupGroup = group;
		return self();
	}

	/**
	 * Sets the group and sub-group for the action in the pop-up menu.  Actions in the same group
	 * will appear next to other actions in the same group and actions in different groups will
	 * be separated by menu dividers.  The sub-group is used to order the actions within the group
	 * 
	 * @param group the group used to clump actions together.
	 * @param subGroup the sub-group used to order actions within a group.
	 * @return this builder (for chaining)
	 */
	public B popupMenuGroup(String group, String subGroup) {
		popupSubGroup = group;
		return self();
	}

	/**
	 * Sets the icon to use in this action's pop-up menu item.
	 * 
	 * @param icon the icon to use in the action's pop-up menu item. 
	 * @return this builder (for chaining)
	 */
	public B popupMenuIcon(Icon icon) {
		popupIcon = icon;
		return self();
	}

	/**
	 * Sets the icon to use in this action's tool bar button.  Setting this attribute is what 
	 * causes the action to appear on the tools or component provider's action tool bar.
	 * 
	 * @param icon the icon to use in the action's tool bar.
	 * @return this builder (for chaining)
	 */
	public B toolBarIcon(Icon icon) {
		toolbarIcon = icon;
		return self();
	}

	/**
	 * Sets the path for the icon to use in this action's tool bar button.  Setting this attribute
	 * causes the action to appear on the tools or component provider's action tool bar.
	 * 
	 * @param iconFilepath the module relative path for the icon to use in the action's tool bar.
	 * @return this builder (for chaining)
	 */
	public B toolBarIcon(String iconFilepath) {
		toolbarIcon = ResourceManager.loadImage(iconFilepath);
		return self();
	}

	/**
	 * Sets the group for the action in the tool bar.  Actions in the same group will appear
	 * next to other actions in the same group and actions in different groups will be separated
	 * by menu dividers
	 * 
	 * @param group for this action
	 * @return this builder (for chaining)
	 */
	public B toolBarGroup(String group) {
		toolBarGroup = group;
		return self();
	}

	/**
	 * Sets the group and sub-group for the action in the tool bar.  Actions in the same group
	 * will appear next to other actions in the same group and actions in different groups will
	 * be separated by menu dividers.  The sub-group is used to order the actions within the group
	 * 
	 * @param group the group used to clump actions together.
	 * @param subGroup the sub-group used to order actions within a group.
	 * @return this builder (for chaining)
	 */
	public B toolBarGroup(String group, String subGroup) {
		toolBarSubGroup = group;
		return self();
	}

	/**
	 * Sets the primary callback to be executed when this action is invoked.  This builder will
	 * throw an {@link IllegalStateException} if one of the build methods is called without
	 * providing this callback
	 * 
	 * @param action the callback to execute when the action is invoked
	 * @return this builder (for chaining)
	 */
	public B onAction(Consumer<ActionContext> action) {
		actionCallback = action;
		return self();
	}

	/**
	 * Sets a predicate for dynamically determining the action's enabled state.  If this
	 * predicate is not set, the action's enable state must be controlled directly using the
	 * {@link DockingAction#setEnabled(boolean)} method. See 
	 * {@link DockingActionIf#isEnabledForContext(ActionContext)}
	 *  
	 * @param predicate the predicate that will be used to dynamically determine an action's 
	 * enabled state.
	 * @return this builder (for chaining)
	 */
	public B enabledWhen(Predicate<ActionContext> predicate) {
		enabledPredicate = predicate;
		return self();
	}

	/**
	 * Sets a predicate for dynamically determining if this action should be included in
	 * an impending pop-up menu.  If this predicate is not set, the action's will be included
	 * in an impending pop-up, if it is enabled. See 
	 * {@link DockingActionIf#isAddToPopup(ActionContext)}
	 *  
	 * @param predicate the predicate that will be used to dynamically determine an action's 
	 * enabled state.
	 * @return this builder (for chaining)
	 */
	public B popupWhen(Predicate<ActionContext> predicate) {
		popupPredicate = predicate;
		return self();
	}

	/**
	 * Sets a predicate for dynamically determining if this action is valid for the current 
	 * {@link ActionContext}.  See {@link DockingActionIf#isValidContext(ActionContext)}
	 *  
	 * @param predicate the predicate that will be used to dynamically determine an action's 
	 * validity for a given {@link ActionContext}
	 * @return this builder (for chaining)
	 */
	public B validContextWhen(Predicate<ActionContext> predicate) {
		validContextPredicate = predicate;
		return self();
	}

	/**
	 * Sets a predicate for dynamically determining if this action is valid for the current global 
	 * {@link ActionContext}.  See {@link DockingActionIf#isValidGlobalContext(ActionContext)}
	 *  
	 * @param predicate the predicate that will be used to dynamically determine an action's 
	 * validity for a given global {@link ActionContext}
	 * @return this builder (for chaining)
	 */
	public B validGlobalContextWhen(Predicate<ActionContext> predicate) {
		validGlobalContextPredicate = predicate;
		return self();
	}

	protected void validate() {
		if (actionCallback == null) {
			throw new IllegalStateException(
				"Can't build a DockingAction without an action callback");
		}
	}

	protected void decorateAction(DockingAction action) {
		action.setEnabled(isEnabled);
		action.setDescription(description);

		setMenuData(action);
		setToolbarData(action);
		setPopupMenuData(action);

		if (helpLocation != null) {
			action.setHelpLocation(helpLocation);
		}

		if (enabledPredicate != null) {
			action.enabledWhen(enabledPredicate);
		}
		if (validContextPredicate != null) {
			action.validContextWhen(validContextPredicate);
		}
		if (validGlobalContextPredicate != null) {
			action.validGlobalContextWhen(validGlobalContextPredicate);
		}
		if (popupPredicate != null) {
			action.popupWhen(enabledPredicate);
		}
	}

	protected boolean isPopupAction() {
		return popupPath != null;
	}

	protected boolean isToolbarAction() {
		return toolbarIcon != null;
	}

	protected boolean isMenuAction() {
		return menuPath != null;
	}

	private void setPopupMenuData(DockingAction action) {
		if (isPopupAction()) {
			action.setPopupMenuData(new MenuData(popupPath, popupIcon, popupGroup,
				MenuData.NO_MNEMONIC, popupSubGroup));
		}
	}

	private void setToolbarData(DockingAction action) {
		if (isToolbarAction()) {
			action.setToolBarData(new ToolBarData(toolbarIcon, toolBarGroup, toolBarSubGroup));
		}
	}

	private void setMenuData(DockingAction action) {
		if (isMenuAction()) {
			action.setMenuBarData(
				new MenuData(menuPath, menuIcon, menuGroup, menuMnemonic, menuSubGroup));
		}
	}

}
