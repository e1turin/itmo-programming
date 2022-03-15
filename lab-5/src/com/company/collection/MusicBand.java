package com.company.collection;

import java.time.Clock;

public class MusicBand {
    Integer id;
    String name;
    Coordinates coordinates;
    java.time.LocalDate creationDate = java.time.LocalDate.now(Clock.systemUTC()); //automatic setup
    // TODO: how does it work???
    int numberOfParticipants; //TODO >0
    long albumsCount; //TODO >0
    java.util.Date establishmentDate; //TODO nullable
    MusicGanre genre;
    Label label;

    public MusicBand(int id,
                     String name,
                     Coordinates coordinates,
                     int numberOfParticipants,
                     long albumsCount,
                     java.util.Date establishmentDate,
                     MusicGanre genre,
                     Label label) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.numberOfParticipants = numberOfParticipants;
        this.albumsCount = albumsCount;
        this.establishmentDate = establishmentDate;
        this.genre = genre;
        this.label = label;

    }
}
