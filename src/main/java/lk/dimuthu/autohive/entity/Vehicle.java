package lk.dimuthu.autohive.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // JSON එකේදී User ගේ විස්තර ආයේ load වෙන එක නවත්තනවා
    private User user;

    private String make; // උදා: Toyota, Nissan
    private String model; // උදා: Corolla, Leaf
    private Integer year; // උදා: 2018

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public Integer getYear() {
        return year;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
