package com.Ease.Importation;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.HttpServletException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "IMPORTED_ACCOUNT_INFORMATION")
public class ImportedAccountInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Transient
    private String deciphered_value;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "imported_account_id")
    private ImportedAccount importedAccount;

    public ImportedAccountInformation() {

    }

    public ImportedAccountInformation(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public ImportedAccountInformation(String name, String value, ImportedAccount importedAccount) {
        this.name = name;
        this.value = value;
        this.importedAccount = importedAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDeciphered_value() {
        return deciphered_value;
    }

    public void setDeciphered_value(String deciphered_value) {
        this.deciphered_value = deciphered_value;
    }

    public ImportedAccount getImportedAccount() {
        return importedAccount;
    }

    public void setImportedAccount(ImportedAccount importedAccount) {
        this.importedAccount = importedAccount;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("name", this.getName());
        res.put("value", this.getDeciphered_value());
        return res;
    }

    public void decipher(String symmetric_key) throws HttpServletException {
        this.setDeciphered_value(AES.decrypt(this.getValue(), symmetric_key));
    }
}
