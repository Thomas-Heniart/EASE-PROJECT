package com.Ease.Importation;

import com.Ease.Catalog.Website;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "IMPORTED_ACCOUNT")
public class ImportedAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @OneToOne
    @JoinColumn(name = "website_id")
    private Website website;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "importedAccount", cascade = CascadeType.ALL)
    private Set<ImportedAccountInformation> importedAccountInformationSet = new HashSet<>();

    public ImportedAccount() {

    }

    public ImportedAccount(String url, Website website, String name) {
        this.url = url;
        this.website = website;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ImportedAccountInformation> getImportedAccountInformationSet() {
        return importedAccountInformationSet;
    }

    public void setImportedAccountInformationSet(Set<ImportedAccountInformation> importedAccountInformationSet) {
        this.importedAccountInformationSet = importedAccountInformationSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImportedAccount that = (ImportedAccount) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
