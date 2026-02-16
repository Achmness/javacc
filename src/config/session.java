// Session.java
package config;

public class session {

    private static session instance;

    // Account basic info
    private int accId;
   
    private String email;
    private String type;
    private String status;

    // Full account details
    private String fname;
    private String lname;
    private String contact;
    private String address;
    private String username;
    
    private int apId;


    // Private constructor (Singleton)
    public session() {}

    // Get instance (thread safe like your prof)
    public static synchronized session getInstance() {
        if (instance == null) {
            instance = new session();
        }
        return instance;
    }

    // Check if session exists
    public static boolean isInstanceEmpty() {
        return instance == null;
    }

    // ======================
    // SETTERS
    // ======================

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Optional: Set everything at once
    public void setFullDetails(int accId, String username, String email, String type,
                               String status, String fname, String lname,
                               String contact, String address) {

        this.accId = accId;
        this.username = username;
        this.email = email;
        this.type = type;
        this.status = status;
        this.fname = fname;
        this.lname = lname;
        this.contact = contact;
        this.address = address;
    }
    
    public void setApId(int apId) {
    this.apId = apId;
    }


    // ======================
    // GETTERS
    // ======================

    public int getAccId() { return accId; }
    public String getEmail() { return email; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getContact() { return contact; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
    public int getApId() { return apId; }


    // Clear session (logout)
    public void clear() {
        instance = null;
    }
}
