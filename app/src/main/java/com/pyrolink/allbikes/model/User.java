package com.pyrolink.allbikes.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User
{
    public static Map<String, User> USERS = new HashMap<>();

    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String city;
    private Date birthDate;
    private ArrayList<String> leisures;

    public User(String id, String firstName, String lastName, String email, String city, Date birthDate,
                ArrayList<String> leisures)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
        this.birthDate = birthDate;
        this.leisures = leisures;

        USERS.put(id, this);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date birthDate)
    {
        this.birthDate = birthDate;
    }

    public ArrayList<String> getLeisures()
    {
        return leisures;
    }

    public void setLeisures(ArrayList<String> leisures)
    {
        this.leisures = leisures;
    }
}
