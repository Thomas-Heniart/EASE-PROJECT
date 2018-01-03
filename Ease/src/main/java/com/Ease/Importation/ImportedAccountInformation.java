package com.Ease.Importation;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "imported_account_id")
    private ImportedAccount importedAccount;

    public ImportedAccountInformation() {

    }

    public ImportedAccountInformation(String name, String value) {
        this.name = name;
        this.value = value;
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

    public ImportedAccount getImportedAccount() {
        return importedAccount;
    }

    public void setImportedAccount(ImportedAccount importedAccount) {
        this.importedAccount = importedAccount;
    }
}
