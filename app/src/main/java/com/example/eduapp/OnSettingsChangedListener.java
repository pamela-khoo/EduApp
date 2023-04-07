package com.example.eduapp;

public interface OnSettingsChangedListener {
    // Used to implement callback for background music between SettingsFragment and MainActivity
    public void playMusic(boolean bgm);
    public void setDarkMode(boolean dm);
    public void setSoundFX(boolean sfx);
}
