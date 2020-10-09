/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.salesreader;

import com.mycompany.providers.HandleFiles;

/**
 *
 * @author delmar
 */
public class SalesReader {

    public static HandleFiles handleFiles;

    /**
     * Run the application SalesReader
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        handleFiles = new HandleFiles();

        handleFiles.processExistingFiles();
        handleFiles.keepWatching();
    }

}
