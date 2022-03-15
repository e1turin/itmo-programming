package com.company.util;

import com.company.collection.Coordinates;
import com.company.collection.MusicBand;
import com.company.exceptions.InvalidInputException;

import java.io.*;
import java.util.Scanner;

public class IOStream implements IFileInput, IFileOutput, IStdIO {
    private Reader reader;
    private Writer writer;
    boolean interactive = false;
    private Scanner scanner;

    public IOStream(Reader reader, Writer writer, boolean interactive) {
        this.reader = reader;
        this.scanner = new Scanner(reader);
        this.writer = writer;
        this.interactive = interactive;
    }

    public void writeln(String message) {
        this.write(message + "\n");
    }

    public void write(String message) {
        try {
            writer.write(message);
            writer.flush();
        }catch (IOException e){
            System.err.println("<<Ошибка вывода>>");
        }
    }

    boolean yesAnswer(){

    }




}
