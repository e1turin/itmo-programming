package com.company.commands;

import com.company.collection.MusicBandStorage;
import com.company.container.Message;
import com.company.util.IOStream;

public abstract class Command {
    protected final MusicBandStorage target;
    protected final String cmdName;
    protected final String description;
    protected final IOStream ioStream;

    public Command(MusicBandStorage target, String cmdName, IOStream ioStream, String description) {
        this.target = target;
        this.cmdName = cmdName;
        this.ioStream = ioStream;
        this.description = description;
    }

    public String getCmdName() {
        return cmdName;
    }

    public String getDescription() {
        return description;
    }

    public abstract Message exec(Message message);

    // Message help(); -> ??? description


}
