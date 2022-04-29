package com.pyrolink.allbikes.model;

public class Note
{
    private int id;
    private User author;
    private int note;
    private String commentary;

    public int getId()
    {
        return id;
    }

    public int getNote()
    {
        return note;
    }

    public void setNote(int note)
    {
        this.note = note;
    }

    public String getCommentary()
    {
        return commentary;
    }

    public void setCommentary(String commentary)
    {
        this.commentary = commentary;
    }

    public User getAuthor()
    {
        return author;
    }

    public void setAuthor(User author)
    {
        this.author = author;
    }
}
