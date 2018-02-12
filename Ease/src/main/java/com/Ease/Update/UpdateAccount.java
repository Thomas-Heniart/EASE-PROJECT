package com.Ease.Update;

import com.Ease.Utils.HttpServletException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "UPDATE_ACCOUNT")
public class UpdateAccount {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "updateAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UpdateAccountInformation> updateAccountInformationSet = new HashSet<>();

    public UpdateAccount() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UpdateAccountInformation> getUpdateAccountInformationSet() {
        return updateAccountInformationSet;
    }

    public void setUpdateAccountInformationSet(Set<UpdateAccountInformation> updateAccountInformationSet) {
        this.updateAccountInformationSet = updateAccountInformationSet;
    }

    public void addUpdateAccountInformation(UpdateAccountInformation updateAccountInformation) {
        this.getUpdateAccountInformationSet().add(updateAccountInformation);
    }

    public void removeUpdateAccountInformation(UpdateAccountInformation updateAccountInformation) {
        this.getUpdateAccountInformationSet().remove(updateAccountInformation);
    }

    public void decipher(String private_key) throws HttpServletException {
        for (UpdateAccountInformation updateAccountInformation : this.getUpdateAccountInformationSet())
            updateAccountInformation.decipher(private_key);
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        this.getUpdateAccountInformationSet().forEach(updateAccountInformation -> res.put(updateAccountInformation.getName(), updateAccountInformation.getDeciphered_value()));
        return res;
    }

    public boolean match(JSONObject account_information) {
        for (UpdateAccountInformation updateAccountInformation : this.getUpdateAccountInformationSet()) {
            String value = account_information.optString(updateAccountInformation.getName());
            if (value.equals(""))
                return false;
            if (updateAccountInformation.getName().equals("password")) {
                if (updateAccountInformation.getDeciphered_value().equals(value))
                    return false;
            } else if (!updateAccountInformation.getDeciphered_value().equals(value))
                return false;
        }
        return true;
    }

    public void edit(JSONObject account_information, String publicKey) throws HttpServletException {
        for (UpdateAccountInformation updateAccountInformation : this.getUpdateAccountInformationSet())
            updateAccountInformation.edit(account_information.getString(updateAccountInformation.getName()), publicKey);
    }
}