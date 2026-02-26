package config;

public class session {

    private static session instance;

    // Account details - matching your singleton structure
    private int id;
    private String fname;
    private String lname;
    private String email;
    private String username;
    private String status;
    private String type;
    private String contact;
    private String address;
    private String image;
    private int apId;

    // Private constructor (Strict Singleton)
    private session() {}

    public static synchronized session getInstance() {
        if (instance == null) {
            instance = new session();
        }
        return instance;
    }

    public static boolean isInstanceEmpty() {
        return instance == null;
    }

    // Fixed Setters to ensure no data overwriting
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

    // Getters and Setters
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
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public int getApId() { return apId; }
    public void setApId(int apId) { this.apId = apId; }

    // Clear session for logout
    public void clear() {
        instance = null;
    }
}