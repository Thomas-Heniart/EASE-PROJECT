var React = require('react');

function CompanyInformationForm(props){
  return (
      <form class="display-flex flex_direction_column" onSubmit={props.onSubmit}>
        <label htmlFor="contry">Country*</label>
        <select placeholder="Country"
                value={props.companyInfo.country}
                onChange={props.handleCompanyInfoInput}
                id="country"
                name="country"
                class="select_unstyle modal_input"
                required>
          <option value="France">France</option>
        </select>
        <label htmlFor="company_name">Company name*</label>
        <input placeholder="Company name"
               value={props.companyInfo.company_name}
               onChange={props.handleCompanyInfoInput}
               id="company_name"
               name="company_name"
               type="text"
               class="input_unstyle modal_input"
               required/>
        <label htmlFor="street_address">Street Address*</label>
        <input placeholder="Street address"
               value={props.companyInfo.street_address}
               onChange={props.handleCompanyInfoInput}
               id="street_address"
               name="street_address"
               type="text"
               class="input_unstyle modal_input"
               required/>
        <label htmlFor="unit">Suite/Unit</label>
        <input placeholder="Suite/Unit"
               value={props.companyInfo.unit}
               onChange={props.handleCompanyInfoInput}
               id="unit"
               name="unit"
               type="text"
               class="input_unstyle modal_input"/>
        <label htmlFor="city">City*</label>
        <input placeholder="City"
               value={props.companyInfo.city}
               onChange={props.handleCompanyInfoInput}
               id="city"
               name="city"
               type="text"
               class="input_unstyle modal_input"
               required/>
        <label htmlFor="state">State/Province/Region</label>
        <input placeholder="State/Province/Region"
               value={props.companyInfo.state}
               onChange={props.handleCompanyInfoInput}
               id="state"
               name="state"
               type="text"
               class="input_unstyle modal_input"/>
        <label htmlFor="postal_code">Postal Code*</label>
        <input placeholder="Postal Code"
               value={props.companyInfo.zip}
               onChange={props.handleCompanyInfoInput}
               id="zip"
               name="zip"
               type="text"
               class="input_unstyle modal_input"
                required/>
        <label htmlFor="vat_id">VAT ID</label>
        <input placeholder="VAT ID"
               value={props.companyInfo.vat_id}
               onChange={props.handleCompanyInfoInput}
               id="vat_id"
               name="vat_id"
               type="text"
               class="input_unstyle modal_input"/>
        <button class="button-unstyle big-button"
                type="submit">
          Add a Payment Method
        </button>
      </form>
  )
}

module.exports = CompanyInformationForm;