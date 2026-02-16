package config;

public class singleton {
    private static singleton instance;

    // Account details
    private int id;
    private String fname;
    private String lname;
    private String email;
    private String username;
    private String status;
    private String contact;  // added
    private String address;  // added
    private int apId;
    private singleton() {
        // Private constructor to prevent instantiation
    }

    public static synchronized singleton getInstance() {
        if (instance == null) {
            instance = new singleton();
        }
        return instance;
    }

    public static boolean isInstanceEmpty() {
        return instance == null;
    }

    // ============================
    // Full details setter (like Session)
    // ============================
    public void setFullDetails(int id, String username, String email, String status,
                               String fname, String lname, String contact, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.fname = fname;
        this.lname = lname;
        this.contact = contact;
        this.address = address;
    }

    // ============================
    // Getters & Setters
    // ============================
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }

    public String getLname() { return lname; }
    public void setLname(String lname) { this.lname = lname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public int getApId() { return id; }
    public void setApId(int id) { this.id = id; }
}
