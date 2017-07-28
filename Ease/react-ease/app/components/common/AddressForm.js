var React = require('react');

function AddressForm(props){
  return (
      <form class="display-flex flex_direction_column">
        <input placeholder="Company name" type="text" class="input_unstyle modal_input" required/>
        <input placeholder="Street address" type="text" class="input_unstyle modal_input" required/>
        <input placeholder="Suite/Unit" type="text" class="input_unstyle modal_input" required/>
        <input placeholder="City" type="text" class="input_unstyle modal_input" required/>
        <input placeholder="State/Province/Region" type="text" class="input_unstyle modal_input" required/>
        <input placeholder="Postal Code" type="text" class="input_unstyle modal_input" required/>
        <input placeholder="VAT ID" type="text" class="input_unstyle modal_input" required/>
      </form>
  )
}

module.exports = AddressForm;