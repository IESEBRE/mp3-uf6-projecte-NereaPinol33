package org.example.model.entities;

import java.util.Collection;
import java.util.TreeSet;

public class Recepta {

    private Long id;
    private String nom;
    private double temps;

    private Collection<Receptes> receptes;

    public Recepta(){}

    public Recepta(String nom, double temps, Collection<Receptes> receptes) {
        this.nom = nom;
        this.temps = temps;
        this.receptes = receptes;
    }

    public Recepta(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Recepta(long id, String nom, double temps) {
        this.id = id;
        this.nom = nom;
        this.temps = temps;
    }

    public Recepta(long id, String nom, double temps, TreeSet<Receptes> receptes) {
        this.id = id;
        this.nom = nom;
        this.temps = temps;
        this.receptes = receptes;
    }

    public Collection<Recepta.Receptes> getReceptes() {
        return receptes;
    }

    private void setReceptes(Collection<Receptes> receptes) {
        this.receptes = receptes;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getTemps() {
        return temps;
    }

    public void setTemps(double temps) {
        this.temps = temps;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public static class Receptes implements Comparable<Receptes> {

        @Override
        public int compareTo(Receptes o) {
            return this.ingedient.compareTo(o.getIngedient());
        }

        public static enum Ingedient {

            //Lletra a
            Aigua(""), Alvocat(""), All(""), Albercocs(""),
            Atmelles(""), Anous(""),
            //Lletra b
            //Lletra c
            Cacau(""),
            //lletra d
            Dàtils(""),
            //Lletra e
            //Lletra f
            Farina(""), Formatge(""),
            //Lletra g
            Galletes(""), Gelatina(""), Güisqui(""),
            //Lletra h
            //Lletra i
            Iogurt(""),
            //Lletra j
            Jolivert(""),
            //Lletra k
            Kaki(""),
            //Lletra l
            Llimona(""), Llet(""), Llevat(""),
            //Lletra m
            Mel(""), Mantega(""), Maduixa(""), Magrana(""), Melmelada(""), Meló(""), Menta(""),
            //Lletra n
            Nata(""),
            //Lletra o
            Oli(""), Ous(""),
            //Lletra p
            Pa(""), Poma(""), Pinya(""), Plàtan(""), Pernil(""),
            //Lletra q
            //Lletra r
            Requesó(""),
            //Lletra s
            Súcre(""), SúcreGlas(""), Síndria(""),
            //Lletra t
            Tomàquet(""), Tofu(""),
            //Lletra u
            //Lletra v
            Vainilla(""),
            //Lletra w
            //Lletra x
            Xocolata(""),
            //Lletra y
            Yuca(""),
            //Lletra z
            Zarzamora("");

            private String nom;

            private Ingedient(String nom) {
                this.nom = nom;
            }

            public String getNom() {
                return nom;
            }

            @Override
            public String toString() {
                return this.name();
            }
        }

        private Receptes.Ingedient ingedient;
        private int nota;

        public Receptes(Receptes.Ingedient ingedient, int nota) {
            this.ingedient = ingedient;
            this.nota = nota;
        }

        public Receptes.Ingedient getIngedient() {
            return ingedient;
        }

        public void setIngedient(Receptes.Ingedient ingedient) {
            this.ingedient = ingedient;
        }

        public int getNota() {
            return nota;
        }

        public void setNota(int nota) {
            this.nota = nota;
        }
    }
}
