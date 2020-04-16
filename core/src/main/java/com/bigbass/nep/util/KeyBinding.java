package com.bigbass.nep.util;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;


public class KeyBinding {
    private int[] keys;
    private Input input;
    private String help;
    private Callable<Void> callback;
    private boolean hold;

    public KeyBinding() {
        this.input = Gdx.input;
        this.callback = () -> {return null;};
        this.help = "No help here :(";
        this.keys = new int[0];
        this.hold = false;
    }

    private void sort() {
        Arrays.sort(this.keys);
        // reverse
        for(int i = 0; i < this.keys.length / 2; i++) {
            int temp = this.keys[i];
            this.keys[i] = this.keys[this.keys.length - i - 1];
            this.keys[this.keys.length - i - 1] = temp;
        }
    }

    public KeyBinding setKeys(int... keys) {
        this.keys = keys.clone();
        this.sort();
        return this;
    }

    public KeyBinding setCallback(Callable<Void> callback) {
        this.callback = callback;
        return this;
    }

    public KeyBinding setHelp(String help) {
        this.help = help;
        return this;
    }

    public KeyBinding setHold(boolean hold) {
        this.hold = hold;
        return this;
    }

    public String toString() {
        return Arrays.stream(this.keys)
                .boxed()
                .map(x -> Input.Keys.toString(x))
                .collect(Collectors.joining(" + "));
    }

    public boolean pressed() {
        for (int key : this.keys) {
            if (!this.input.isKeyPressed(key)) {
                return false;
            }
        }
        return true;
    }

    public boolean justPressed() {
        if (!this.pressed()) {
            return false;
        }

        boolean justPressed = false;
        for (int key : this.keys) {
            if (this.input.isKeyJustPressed(key)) {
                justPressed = true;
            }
        }
        return justPressed;
    }

    public void act() throws Exception {
        if (this.hold) {
            if (this.pressed()) {
                this.callback.call();
            }
        } else {
            if (this.justPressed()) {
                this.callback.call();
            }
        }
    }

    public String getHelp() {
        return this.help;
    }
}