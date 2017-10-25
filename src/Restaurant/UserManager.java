package Restaurant;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is in charge of managing the users by having them stored in a
 * hashmap.
 *
 * @author AlexandruAmarandeiStanescuAAS1U16
 */
public class UserManager {

    private HashMap<String, Integer> loggedInUsers;
    private HashMap<String, Double> postcodes;
    private static final String DEFAULTPATHUSERS = System.getProperty("user.dir") + "\\users";
    private static final String DEFAULTPATHSERVER = System.getProperty("user.dir") + "\\server";

    public UserManager() {
        loggedInUsers = new HashMap<>();
        postcodes = new HashMap<>();
        loadPostCodes(DEFAULTPATHSERVER);
        createFolders();
    }

    public final void createFolders() {
        File users = new File(DEFAULTPATHUSERS);
        if (!users.exists()) {
            users.mkdir();
        }

        File server = new File(DEFAULTPATHSERVER);
        if (!server.exists()) {
            server.mkdir();
        }
        File emails = new File(DEFAULTPATHSERVER + "\\emails");
        File orders = new File(DEFAULTPATHSERVER + "\\orders");
        if (!emails.exists()) {
            emails.mkdir();
        }
        if (!orders.exists()) {
            orders.mkdir();
        }
        users = new File(DEFAULTPATHSERVER + "\\users");
        if (!users.exists()) {
            users.mkdir();
        }
    }

    public final void loadPostCodes(String path) {
        path = path + "\\postcodes.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                String postcode = line.substring(0, line.indexOf(' '));
                Double distance = Double.parseDouble(line.substring(line.indexOf(' ') + 1));
                postcodes.put(postcode, distance);
                line = br.readLine();

            }
            if (br != null) {
                br.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getFolderPathOfUser(String username) {
        String length = Integer.toString(username.length());
        String firstLetter = Character.toString(username.charAt(0));
        String path = DEFAULTPATHSERVER + "\\users\\" + length + firstLetter;
        return path;
    }

    public String getFolderPathOfEmail(String email) {
        String length = Integer.toString(email.length());
        String firstLetter = Character.toString(email.charAt(0));
        String path = DEFAULTPATHSERVER + "\\emails\\" + length + firstLetter;
        return path;
    }

    public String getFolderPathOfUserData(String username) {
        String path = DEFAULTPATHUSERS + "\\" + username;
        return path;
    }

    public boolean checkIfUserIsLoggedOn(String username) {
        return loggedInUsers.containsKey(username);
    }

    public boolean checkIfThreadExists(Integer thread) {
        return loggedInUsers.containsValue(thread);
    }

    public void addOrderIDToUser(String username) {
        String path = getFolderPathOfUser(username);
    }

    public ArrayList<String> getUserData(String username) {
        String path = getFolderPathOfUserData(username);
        ArrayList<String> data = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            path = path + "\\data.txt";
            try {
                BufferedReader br = new BufferedReader(new FileReader(path));
                String line = br.readLine();
                while (line != null) {
                    data.add(line);
                    line = br.readLine();
                }
                if (br != null) {
                    br.close();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return data;
    }

    public String getUserPassword(String username) {
        ArrayList<String> data = getUserData(username);
        return data.get(1);
    }

    public String getUserName(String username) {
        ArrayList<String> data = getUserData(username);
        return data.get(2);
    }

    public String getUserAddress(String username) {
        ArrayList<String> data = getUserData(username);
        return data.get(3);
    }

    public String getUserEmail(String username) {
        ArrayList<String> data = getUserData(username);
        return data.get(4);
    }

    public String getUserPostcode(String username) {
        ArrayList<String> data = getUserData(username);
        return data.get(5);
    }

    public boolean checkIfUserExists(String username) {
        String path = getFolderPathOfUser(username);
        File dir = new File(path);
        if (dir.exists()) {
            path = path + "\\usernames.txt";
            File nameFile = new File(path);
            if (nameFile.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(path));
                    String line = br.readLine();
                    while (line != null) {
                        if (line.substring(0, line.indexOf(' ')).equals(username)) {
                            return true;
                        }
                        line = br.readLine();

                    }
                    if (br != null) {
                        br.close();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return false;

    }

    public boolean checkLogInInfo(String username, String password, int ID) {
        if (checkIfUserExists(username)) {
            if (password.equals(getUserPassword(username))) {
                loggedInUsers.put(username, ID);
                return true;
            }
        }
        return false;
    }

    public Set<String> getPostCodes() {
        return postcodes.keySet();
    }

    public Integer getLoggedInClientSesionNumber(String username) {
        if (loggedInUsers.containsKey(username)) {
            return loggedInUsers.get(username);
        }
        return -1;
    }

    public String getPostCodesAsString() {
        String[] pc = postcodes.keySet().toArray(new String[0]);
        String returnString = "";
        for (String st : pc) {
            returnString = returnString + "$" + st;
        }
        return returnString;
    }

    public String getPostCodeForUser(String username) {
        ArrayList<String> data = getUserData(username);
        return data.get(5);
    }

    public ArrayList<String> getOrdersForUser(String username) {

        String path = getFolderPathOfUserData(username);
        ArrayList<String> data = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            path = path + "orders.txt";
            try {
                BufferedReader br = new BufferedReader(new FileReader(path));
                String line = br.readLine();
                while (line != null) {
                    data.add(line);
                    line = br.readLine();
                }
                if (br != null) {
                    br.close();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return data;
    }

    public Double getDistance(String username) {
        return postcodes.get(getPostCodeForUser(username));
    }

    public void writeorderIDForUser(String username, String orderID) {
        String path = getFolderPathOfUser(username);
        writeLineInFile(path + "\\orders.txt", orderID, true);
    }

    public void loggedOut(String username) {
        loggedInUsers.remove(username);
    }

    public boolean checkIfEmailExists(String email) {
        String path = getFolderPathOfUser(email);
        File dir = new File(path);
        if (dir.exists()) {
            path = path + "\\emails.txt";
            File nameFile = new File(path);
            if (nameFile.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(path));
                    String line = br.readLine();
                    while (line != null) {
                        if (line.substring(0, line.indexOf(' ')).equals(email)) {
                            return true;
                        }
                        line = br.readLine();

                    }
                    if (br != null) {
                        br.close();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return false;
    }

    private void writeLineInFileAndCreateFile(String path, String toWrite) {
        String[] stArray = new String[1];
        stArray[0] = toWrite;
        writeLineInFile(path, stArray, true);
    }

    private void writeLineInFileAndCreateFile(String path, String[] toWrite) {
        writeLineInFile(path, toWrite, true);
    }

    private void writeLineInFile(String path, String toWrite, boolean create) {
        String[] stArray = new String[1];
        stArray[0] = toWrite;
        writeLineInFile(path, stArray, create);
    }

    private void writeLineInFile(String path, String[] toWrite, boolean create) {
        File file = new File(path);

        if (!file.exists()) {
            if (create) {
                try {
                    PrintWriter pw = new PrintWriter(path, "UTF-8");
                    pw.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (file.exists()) {
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
                for (String line : toWrite) {
                    pw.println(line);
                    pw.flush();
                }

                if (pw != null) {
                    pw.close();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addUser(String username, String password, String name,
            String address, String email, String postcode) {
        if (!checkIfUserExists(username)) {
            String path = getFolderPathOfUser(username);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdir();
            }

            path = path + "\\usernames.txt";
            writeLineInFile(path, username + " " + email, true);

            String emailPath = getFolderPathOfEmail(email);

            dir = new File(emailPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            writeLineInFile(emailPath + "\\emails.txt", email + " " + username, true);

            path = getFolderPathOfUserData(username);
            File userData = new File(path);

            if (!userData.exists()) {
                userData.mkdir();
                String[] data = new String[6];
                data[0] = username;
                data[1] = password;
                data[2] = name;
                data[3] = address;
                data[4] = email;
                data[5] = postcode;
                writeLineInFile(path + "\\data.txt", data, true);
                writeLineInFileAndCreateFile(path + "\\orders.txt", new String[0]);
            }
        }
    }
}
