package com.company.collection;

import java.util.LinkedHashSet;

public class MusicBandStorage {
    //TODO logics
    LinkedHashSet<MusicBand> storage;

    public MusicBandStorage() {
        this.storage = new LinkedHashSet<>();
    }

    public void add(MusicBand musicBand){
        storage.add(musicBand);
    }

}
