import React from 'react';
import {Elements} from 'react-stripe-elements';
import {injectStripe} from 'react-stripe-elements';
import {teamAddCreditCard} from "../../actions/teamActions";
import {connect} from "react-redux";
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';
import {CardElement, CardNumberElement, CardExpiryElement, CardCVCElement, PostalCodeElement} from 'react-stripe-elements';

@connect()
class CheckoutForm extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      errorMessage: '',
      loading: false,
      name: ''
    };
  }
  handleInput = (e, {name, value}) => {
    this.setState({[name]: value});
  };
  handleSubmit = (ev) => {
    // We don't want to let default form submission happen here, which would refresh the page.
    ev.preventDefault();
    this.setState({errorMessage: '', loading: true});
    // Within the context of `Elements`, this call to createToken knows which Element to
    // tokenize, since there's only one in this group.
    this.props.stripe.createToken({name: this.state.name}).then(({token, error}) => {
      if (error){
        this.setState({errorMessage: error.message, loading: false});
        return;
      }
      this.props.dispatch(teamAddCreditCard({
        team_id: this.props.team_id,
        cardToken: token.id
      })).then(response => {
        this.setState({loading: false});
        this.props.validate(token);
      }).catch(err => {
        this.setState({errorMessage: err, loading: false});
      });
    });
    // However, this line of code will do the same thing:
    // this.props.stripe.createToken({type: 'card', name: 'Jenny Rosen'});
  };

  render() {
    return (
        <Form onSubmit={this.handleSubmit} size="mini" error={this.state.errorMessage.length > 0}>
          <Form.Input
              width={8}
              required
              type="text"
              label="Name on card"
              value={this.state.name}
              onChange={this.handleInput}
              name="name"
              placeholder="Name on card"/>
          <Form.Field className="display-flex flex_direction_column">
            <label>Card number</label>
            <CardElement style={{base: {fontSize: '18px'}}} />
          </Form.Field>
          <Message error content={this.state.errorMessage}/>
          <Form.Field className="overflow-hidden">
            <Button primary content='Save' floated="right" type="submit" size="mini" loading={this.state.loading}/>
            <Button type="button" onClick={this.props.cancel} content="Cancel" floated="right" size="mini"/>
          </Form.Field>
        </Form>
    );
  }
}

const StripeInjectedForm = injectStripe(CheckoutForm);

class MyStoreCheckout extends React.Component {
  render() {
    return (
        <Elements>
          <StripeInjectedForm team_id={this.props.team_id} cancel={this.props.cancel} validate={this.props.validate}/>
        </Elements>
    );
  }
}

module.exports = MyStoreCheckout;
