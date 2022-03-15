package com.company;

import com.company.collection.MusicBandStorage;
import com.company.collection.StorageManager;
import com.company.commands.AddCmd;
import com.company.util.IOStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws IOException {
        String storageName;

        IOStream stdIOStream = new IOStream(
                new InputStreamReader(System.in, StandardCharsets.UTF_8),
                new PrintWriter(System.out),
                true
        );


        try {
            IOStream fileIOStream = new IOStream(
                    new FileReader(new File(args[1])),
                    new FileWriter(new File(args[1])),
                    true
            );
            storageName = args[1];

        } catch (FileNotFoundException e) { //TODO: usable??
            stdIOStream.writeln("Указан не верный путь до файла, или файла не существует. " +
                    "Создать файл? - Y[es]/N[o]:");
            if (stdIOStream.yesAnswer()) {
                storageName = stdIOStream.readFileName();
            } else {
                stdIOStream.writeln("Выход из консоли управления...");
                return;
            }


        MusicBandStorage storage = new MusicBandStorage();
        StorageManager storageManager = new StorageManager(
                storage,
                stdIOStream,
                new AddCmd(storage, "add", stdIOStream)
                // new RemoveCmd(...)
                // new SaveCmd(storage, "save", fileIOStream),
                // ...
        );

        while (true) {


        }


    }

}
