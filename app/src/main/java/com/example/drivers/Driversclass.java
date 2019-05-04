package com.example.drivers;

import android.support.v7.app.AppCompatActivity;

import java.util.Date;

public class Driversclass extends AppCompatActivity{
        private String name;
        private Date joindate,birthdate;
        private int contact;
        private int cnic;
        private String id;

        public Driversclass (String id, int cnic, String name, Date joindate, Date birthdate, int contact) {
            this.cnic = cnic;
            this.birthdate=birthdate;
            this.contact=contact;
            this.joindate=joindate;
            this.name=name;
        }

        public Driversclass()
        {

        }

        public String getname() {
            return name;
        }

        public Date getbirthdate() {
            return birthdate;
        }

        public Date getjoindate() {
            return joindate;
        }

        public int getcontact() {
            return contact;
        }

        public int getcnic() {
            return cnic;
        }
    }
