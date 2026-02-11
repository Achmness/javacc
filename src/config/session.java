package config;

public class session {
    private static session instance;

    // Current fields
    private int accId;
    private String email;
    private String type;
    private String status;

    // Add full account details
    private String fname;
    private String lname;
    private String contact;
    private String address;
    private String username;

    private session() {}

    public static session getInstance() {
        if (instance == null) {
            instance = new session();
        }
        return instance;
    }

    // Method to set basic info (like before)
    public void setAccount(int accId, String email, String type, String status) {
        this.accId = accId;
        this.email = email;
        this.type = type;
        this.status = status;
    }

    // Method to set full details
    public void setFullDetails(String username, String email, String type, String status,
                               String fname, String lname, String contact, String address) {
        this.username = username;
        this.email = email;
        this.type = type;
        this.status = status;
        this.fname = fname;
        this.lname = lname;
        this.contact = contact;
        this.address = address;
    }

    // Getters
    public int getAccId() { return accId; }
    public String getEmail() { return email; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getContact() { return contact; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }

    public void clear() {
        instance = null;
    }
}
