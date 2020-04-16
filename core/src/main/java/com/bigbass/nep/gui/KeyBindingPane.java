package com.bigbass.nep.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.bigbass.nep.gui.KeyBindingManager;
import com.bigbass.nep.gui.KeyBindingTableBuilder;

public class KeyBindingPane {

    private KeyBindingManager bindingManager;
    private Table table;
    private KeyBindingTableBuilder builder;
    private Stage stage;

    public KeyBindingPane(Stage stage, KeyBindingManager bindingManager){
        this.stage = stage;
        this.bindingManager = bindingManager;
        this.table = new Table();
        table.setVisible(false);

        this.builder = new KeyBindingTableBuilder(
                this.stage,
                this.table,
                this.bindingManager,
                600);

        this.builder.build();

        this.stage.addActor(table);
    }

    public void setVisible(boolean val){
        this.table.setVisible(val);
    }

    public boolean isVisible(){
        return this.table.isVisible();
    }

    public void rebuild() {
        this.builder.build();
    }

//    public void resize(int width, int height){
//        builder.reposition();
//    }
//
//    public void dispose(){
//        builder.dispose();
//    }
}
