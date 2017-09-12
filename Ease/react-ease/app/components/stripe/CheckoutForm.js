import React from 'react';
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';
import {CardElement, CardNumberElement, CardExpiryElement, CardCVCElement, PostalCodeElement} from 'react-stripe-elements';
import {injectStripe} from 'react-stripe-elements';

class CheckoutForm extends React.Component {
  handleSubmit = (ev) => {
    // We don't want to let default form submission happen here, which would refresh the page.
    ev.preventDefault();

    // Within the context of `Elements`, this call to createToken knows which Element to
    // tokenize, since there's only one in this group.
    this.props.stripe.createToken({name: 'Jenny Rosen'}).then(({token}) => {
      console.log('Received Stripe token:', token);
    });

    // However, this line of code will do the same thing:
    // this.props.stripe.createToken({type: 'card', name: 'Jenny Rosen'});
  };

  render() {
    return (
        <form onSubmit={this.handleSubmit} style={{width:"100%"}} class="display-flex flex_direction_column">
          <CardElement style={{base: {fontSize: '18px'}}} />
          <button class="button-unstyle big-button">
            Complete your purchase
          </button>
        </form>
    );
  }
}

export default injectStripe(CheckoutForm);