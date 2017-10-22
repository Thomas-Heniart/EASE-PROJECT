package com.Ease.Catalog;

import javax.persistence.*;

@Entity
@Table(name = "serverPublicKeys")
public class ServerPublicKey {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "publicKey")
    private String publicKey;

    public ServerPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public ServerPublicKey() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
