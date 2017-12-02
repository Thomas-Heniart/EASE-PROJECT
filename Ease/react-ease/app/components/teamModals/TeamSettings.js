import React from 'react';
import {editTeamName} from '../../actions/teamActions';
import { Header, Container, Menu, Segment, Comment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';
import {StripeProvider} from 'react-stripe-elements';
import {connect} from "react-redux";
import {fetchTeamPaymentInformation, teamInviteFriends, teamUpdateBillingInformation, unsubscribe} from "../../actions/teamActions";
import countryValues from "../../utils/countrySelectList";
import {withRouter, Switch, Route, NavLink} from "react-router-dom";
var MyStoreCheckout = require('../stripe/MyStoreCheckout');

@connect(store => ({
  teams:store.teams
}))
class TeamInformations extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      modifying: false,
      teamName: '',
      loading: false
    };
  }
  handleInput = (e, {name, value}) => {
    this.setState({[name]: value});
  };
  setModifying = (state) => {
    const team = this.props.teams[this.props.match.params.teamId];
    this.setState({modifying: state, teamName: team.name});
  };
  submit = () => {
    const team = this.props.teams[this.props.match.params.teamId];
    this.setState({loading: true});
    this.props.dispatch(editTeamName({
      team_id: team.id,
      name:this.state.teamName
    })).then(() => {
      this.setState({loading: false});
      this.setModifying(false);
    });
  };
  render(){
    const team = this.props.teams[this.props.match.params.teamId];
    return (
        <div class="team_settings_section">
          <Header as="h3">
            {team.name}
          </Header>
          <Segment>
            <Header as="h4">
              Choose the name displayed for your team
            </Header>
            <Form size="mini" style={{marginBottom: 0}}>
              <Form.Group>
                <Form.Input
                    disabled={!this.state.modifying}
                    value={!this.state.modifying ? team.name : this.state.teamName}
                    onChange={this.handleInput}
                    autoFocus
                    name="teamName"
                    type="text"
                    label="Team Name"
                    placeholder="Team name..."/>
              </Form.Group>
              <Form.Field>
                Your Team Name is displayed in your Team Space.<br/>
                All invitation to new team members will mention your Team Name.<br/>
                The Team Name remains confidential to your team members only.<br/>
              </Form.Field>
              {!this.state.modifying ?
                  <Form.Field style={{overflow: 'hidden'}}>
                    <Button primary size="mini" content="Modify" floated="right" onClick={this.setModifying.bind(null, true)}/>
                  </Form.Field> :
                  <Form.Field style={{overflow: 'hidden'}}>
                    <Button primary size="mini" content="Save" floated="right" type="submit" loading={this.state.loading} onClick={this.submit}/>
                    <Button size="mini" content="Cancel" floated="right" onClick={this.setModifying.bind(null, false)}/>
                  </Form.Field>}
            </Form>
          </Segment>
        </div>
    )
  }
}

function CreditCardPreview({card}){
  return (
      <div class="credit-card-preview display-flex">
        <div class="icon">
          <Icon name="credit card alternative"/>
        </div>
        <div class="info display-flex flex_direction_column">
          <span class="name">{card.name}</span>
          <span class="number">••••••••••••{card.last4}&nbsp;&nbsp;&nbsp;{card.exp_month}/{card.exp_year}</span>
        </div>
      </div>
  )
}

@connect((store)=>({
  payment : store.team_payments
}))
class PaymentMethod extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      modifying: false
    };
  }
  setModifying = (state) => {
    this.setState({modifying: state});
  };
  cardTokenCallback = (token) => {
    //do stuff with token;
    //console.log(token);
    this.setModifying(false);
  };
  render(){
    const payment = this.props.payment;
    const card = payment.data.card;
    const team_id = Number(this.props.match.params.teamId);

    return (
        <div class="team_settings_section">
          <Header as="h3">
            Payment
          </Header>
          <Segment size="mini" loading={payment.loading}>
            <Header as="h4">
              Subscription information
            </Header>
            <p>You can suscribe or unsubscribe whenever you want. Pay as you go, without commitment.</p>
            <p>Only active users are billed each month at the rate of 3,99€ before VAT. Other users are not billed.</p>
            <Header as="h4">
              {card !== null ? 'Credit card information' : 'There is no credit card set up yet'}
            </Header>
            {card !== null && !this.state.modifying &&
            <CreditCardPreview card={card}/>}
            {!this.state.modifying &&
            <div class="overflow-hidden">
              <Button primary size="mini" content={card !== null ? 'Replace this card' : 'Add credit card'} floated="right" onClick={this.setModifying.bind(null, true)}/>
            </div>}
            {this.state.modifying &&
            <MyStoreCheckout
                team_id={team_id}
                cancel={this.setModifying.bind(null, false)}
                validate={this.cardTokenCallback}/>}
          </Segment>
          {<FriendInvitations team_id={team_id}/>}
        </div>
    )
  }
}

@connect((store)=>({
  payment : store.team_payments
}))
class FriendInvitations extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      modifying : false,
      errorMessage: '',
      loading: false,
      email1: '',
      email2: '',
      email3: ''
    };
  }
  handleInput = (e, {name, value}) => {
    this.setState({[name]: value});
  };
  setModifying = (state) => {
    this.setState({modifying: state});
  };
  sendInvitations = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage:''});
    this.props.dispatch(teamInviteFriends({
      team_id: this.props.team_id,
      email1: this.state.email1,
      email2: this.state.email2,
      email3: this.state.email3
    })).then(response => {
      this.setState({loading: false});
      this.setModifying(false);
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render(){
    const payment = this.props.payment;
    return (
        <Segment size="mini" loading={payment.loading}>
          <Header style={{color: '#2185d0'}}>
            Credits : {payment.data.credit} €
          </Header>
          <p>You can obtain credits by inviting friends. As soon as you have credits your payments will be withdrawn from it until you reach 0€.</p>
          {!this.state.modifying &&
          <div class="overflow-hidden">
            <Button disabled={payment.data.people_invited} floated="right" size="mini" primary content="Invite friends" onClick={this.setModifying.bind(null, true)}/>
          </div>}
          {this.state.modifying &&
          <Header>
            Invite 3 friends from other companies to discover Ease.space and get 15€ in credit
          </Header>}
          {this.state.modifying &&
          <Form size="mini" onSubmit={this.sendInvitations} error={this.state.errorMessage.length > 0}>
            <Form.Field>
              You can invite friends only once.<br/>
              Make sure these are the right email adresses, otherwise it won’t work, and you will get charged back.
            </Form.Field>
            <Form.Input width={9}
                        type="email"
                        name="email1"
                        value={this.state.email1}
                        onChange={this.handleInput}
                        required
                        placeholder="friend@company.com"/>
            <Form.Input width={9}
                        type="email"
                        name="email2"
                        value={this.state.email2}
                        onChange={this.handleInput}
                        required
                        placeholder="friend@company.com"/>
            <Form.Input width={9}
                        type="email"
                        name="email3"
                        value={this.state.email3}
                        onChange={this.handleInput}
                        required
                        placeholder="friend@company.com"/>
            <Message error content={this.state.errorMessage}/>
            <Form.Field className="overflow-hidden">
              <Button primary floated="right" loading={this.state.loading} content="Send invitations" size="mini"/>
            </Form.Field>
          </Form>}
        </Segment>
    )
  }
}

@connect((store) => ({
  payments : store.team_payments
}))
class BillingInformation extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      modifying: false,
      loading: false,
      errorMessage: '',
      address_country: '',
      address_city: '',
      address_line1: '',
      address_line2: '',
      address_state: '',
      address_zip: '',
      business_vat_id: ''
    }
  }
  setModifying = () => {
    this.setState({modifying: true});
  };
  unsetModifying = () => {
    this.setState({modifying: false});
  };
  submit = (e) =>{
    const teamId = Number(this.props.match.params.teamId);
    e.preventDefault();
    this.setState({errorMessage: '', loading: true});
    this.props.dispatch(teamUpdateBillingInformation({
      team_id: teamId,
      address_country: this.state.address_country,
      address_city: this.state.address_city,
      address_line1: this.state.address_line1,
      address_line2: this.state.address_line2,
      address_state: this.state.address_state,
      address_zip: this.state.address_zip,
      business_vat_id: this.state.business_vat_id
    })).then(response => {
      this.setState({modifying: false, loading: false});
    }).catch(err => {
      this.setState({errorMessage:err, loading: false});
    });
  };
  handleInput = (e, {name, value}) => {
    if (name !== undefined && value !== undefined)
      this.setState({[name]: value});
    else
      this.setState({[e.target.name]: e.target.value});
  };
  componentWillReceiveProps(nextProps){
    const team_payment = nextProps.payments;
    if (this.props !== nextProps && !team_payment.loading && team_payment.data.card !== null){
      const card = team_payment.data.card;
      this.setState({
        address_country: card.address_country !== null ? card.address_country : '',
        address_city: card.address_city !== null ? card.address_city : '',
        address_line1: card.address_line1 !== null ? card.address_line1 : '',
        address_line2: card.address_line2 !== null ? card.address_line2 : '',
        address_state: card.address_state !== null ? card.address_state : '',
        address_zip: card.address_zip !== null ? card.address_zip : '',
        business_vat_id: team_payment.data.business_vat_id
      });
    }
  }
  componentDidMount(){
    const team_payment = this.props.payments;

    if (!team_payment.loading && team_payment.data.card !== null){
      const card = team_payment.data.card;
      this.setState({
        address_country: card.address_country !== null ? card.address_country : '',
        address_city: card.address_city !== null ? card.address_city : '',
        address_line1: card.address_line1 !== null ? card.address_line1 : '',
        address_line2: card.address_line2 !== null ? card.address_line2 : '',
        address_state: card.address_state !== null ? card.address_state : '',
        address_zip: card.address_zip !== null ? card.address_zip : '',
        business_vat_id: team_payment.data.business_vat_id
      });
    }
  }
  render(){
    const payment = this.props.payments;
    const card = payment.data.card;
    return (
        <div class="team_settings_section">
          <Header as="h3">
            Billing information
          </Header>
          <Segment loading={payment.loading} size="mini">
            <Header as="h4">
              If needed you can fill billing informations
            </Header>
            <p>
              All information filled will be displayed on your monthly bill.
            </p>
            {!payment.loading && card === null &&
            <Message color="red">
              Before adding your billing information, you need to set up your credit card in the&nbsp;
              <NavLink to={`/teams/${this.props.match.params.teamId}/${this.props.match.params.itemId}/settings/payment`}>
                Payment section.
              </NavLink>
            </Message>}
            <Form size="mini" onChange={this.handleInput} onSubmit={this.submit} error={this.state.errorMessage.length > 0}>
              <Form.Select
                  fluid search
                  width={8}
                  disabled={!this.state.modifying}
                  name="address_country"
                  onChange={this.handleInput}
                  value={this.state.address_country}
                  options={countryValues}
                  label="Country"/>
              <Form.Input
                  disabled={!this.state.modifying}
                  width={8}
                  name="address_line1"
                  value={this.state.address_line1}
                  label="Street address"
                  placeholder="Street address"/>
              <Form.Input
                  disabled={!this.state.modifying}
                  width={8}
                  name="address_line2"
                  value={this.state.address_line2}
                  label="Suit/Unit"
                  placeholder="Suit/Unit"/>
              <Form.Input
                  disabled={!this.state.modifying}
                  width={8}
                  name="address_city"
                  value={this.state.address_city}
                  label="City"
                  placeholder="City"/>
              <Form.Input
                  disabled={!this.state.modifying}
                  width={8}
                  name="address_state"
                  value={this.state.address_state}
                  label="State/Province/Region"
                  placeholder="State/Province/Region"/>
              <Form.Input
                  disabled={!this.state.modifying}
                  width={4}
                  name="address_zip"
                  value={this.state.address_zip}
                  label="Postal Code"
                  placeholder="Postal Code"/>
              <Form.Input
                  disabled={!this.state.modifying}
                  name="business_vat_id"
                  value={this.state.business_vat_id}
                  width={4}
                  label="VAT ID"
                  placeholder="VAT ID"/>
              <Message error content={this.state.errorMessage}/>
              {!this.state.modifying &&
              <Form.Button primary disabled={card === null} type="button" onClick={this.setModifying} className="overflow-hidden" content="Modify" floated="right" size="mini"/>}
              {this.state.modifying &&
              <Form.Field className="overflow-hidden">
                <Button type="submit" primary size="mini" floated="right" content="Save" loading={this.state.loading}/>
                <Button type="button" size="mini" floated="right" content="Cancel" onClick={this.unsetModifying}/>
              </Form.Field>}
            </Form>
          </Segment>
        </div>
    )
  }
}
const BillingInformationWithRouter = withRouter(BillingInformation);

@connect()
class TeamAccount extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      modifying: false,
      password: '',
      checked: false,
      loading: false,
      errorMessage: ''
    };
  }
  handleInput = (e, {name, value}) => {
    this.setState({[name]: value});
  };
  check = () => {
    this.setState({checked: !this.state.checked});
  };
  setModifying = (state) => {
    this.setState({modifying: state, password:'', checked: false});
  };
  submit = (e) => {
    e.preventDefault();
    const team_id = Number(this.props.match.params.teamId);
    this.setState({errorMessage: '', loading: true});
    this.props.dispatch(unsubscribe({
      team_id: team_id,
      password:this.state.password
    })).then(response => {
    }).catch(err => {
      this.setState({errorMessage: err, loading: false});
    });
  };
  render(){
    return (
        <div class="team_settings_section">
          <Header as="h3" >
            Team Account
          </Header>
          <Segment size="mini">
            <Header as="h4">
              Your team account is activated
            </Header>
            <div style={{marginBottom:'1rem'}}>
              While you are in free trial or have a credit card set up, your account is activated.<br/>
              Our subscription system is made so that you do not have to worry about unsubscription, you can unsuscribe at anytime. At the beginning of each 30 day period you are billed for the period, so if you unsuscribe you won’t owe us anything and the monthly billing will stop instantly.
            </div>
            {!this.state.modifying &&
            <div class="overflow-hidden">
              <Button content="Unsubscribe" negative floated="right" size="mini" onClick={this.setModifying.bind(null, true)}/>
            </div>}
            {this.state.modifying &&
            <Form error={this.state.errorMessage.length > 0} onSubmit={this.submit} size="mini">
              <Header as="h4">
                Confirm your unsubscription
              </Header>
              <Form.Field>
                Unsuscribing implies a permanent loss of all passwords and IDs secured related to your Team Space. Also all your team members will loose the access to team apps and to the Team Space.
              </Form.Field>
              <Form.Field>
                <Checkbox
                    className="mini"
                    checked={this.state.checked}
                    onClick={this.check}
                    label="I understand the condition of unsubscription"/>
              </Form.Field>
              <Form.Group>
                <Form.Input
                    required
                    label="Your password"
                    type="password"
                    name="password"
                    placeholder="Your password"
                    onChange={this.handleInput}
                    value={this.state.password}/>
              </Form.Group>
              <Message error content={this.state.errorMessage}/>
              <Form.Field className="overflow-hidden">
                <Button negative
                        floated="right"
                        size="mini"
                        disabled={!this.state.checked || this.state.password.length === 0}
                        loading={this.state.loading}
                        type="submit"
                        content="Unsubscribe"/>
                <Button floated="right" size="mini" content='Cancel' onClick={this.setModifying.bind(null, false)}/>
              </Form.Field>
            </Form>}
          </Segment>
        </div>
    )
  }
}

function SettingsMenu(props){
  return (
      <Menu pointing vertical fluid size="mini" style={{marginTop: '43px'}}>
        <Menu.Item as={NavLink} to={`${props.match.url}/information`}>
          <Header as="h4">
            {props.teamName}
          </Header>
          <span>Choose the name of your team</span>
        </Menu.Item>
        <Menu.Item as={NavLink} to={`${props.match.url}/payment`}>
          <Header as="h4">
            Payment
          </Header>
          <span>Subscription information & credit card</span>
        </Menu.Item>
        <Menu.Item as={NavLink} to={`${props.match.url}/billing`}>
          <Header as="h4">
            Billing information
          </Header>
          <span>Optional billing information</span>
        </Menu.Item>
        <Menu.Item as={NavLink} to={`${props.match.url}/activation`}>
          <Header as="h4">
            Team Account
          </Header>
          <span>Information about your account</span>
        </Menu.Item>
      </Menu>
  )
}

const stripe_api_key = window.location.hostname === 'ease.space' ? 'pk_live_lPfbuzvll7siv1CM3ncJ22Bu' : 'pk_test_95DsYIUHWlEgZa5YWglIJHXd';

@connect((store)=>({
  teams : store.teams
}))
class TeamSettings extends React.Component {
  constructor(props) {
    super(props);
  }
  close = () => {
    this.props.history.replace(`/teams/${this.props.match.params.teamId}/${this.props.match.params.itemId}/`);
  };
  componentDidMount(){
    this.props.dispatch(fetchTeamPaymentInformation({team_id: this.props.match.params.teamId}));
    if (this.props.match.isExact)
      this.props.history.replace(`${this.props.match.url}/information`);
  }
  render() {
    return (
        <StripeProvider apiKey={stripe_api_key}>
        <div className="ease_modal" id="team_settings_modal">
          <div className="modal-background"/>
          <a id="ease_modal_close_btn" className="ease_modal_btn" onClick={this.close}>
            <i className="ease_icon fa fa-times"/>
            <span className="key_label">close</span>
          </a>
          <div className="modal_contents_container">
            <div class="contents">
              <div class="content_row justify_content_center">
                <h1>Team settings</h1>
              </div>
              <Grid>
                <Grid.Row>
                  <Grid.Column width={5}>
                    <SettingsMenu teamName={this.props.teams[this.props.match.params.teamId].name} match={this.props.match}/>
                  </Grid.Column>
                  <Grid.Column width={11}>
                    <Switch>
                      <Route exact path={`${this.props.match.path}/`} component={TeamInformations}/>
                      <Route path={`${this.props.match.path}/information`} component={TeamInformations}/>
                      <Route path={`${this.props.match.path}/payment`} component={PaymentMethod}/>
                      <Route path={`${this.props.match.path}/billing`} component={BillingInformationWithRouter}/>
                      <Route path={`${this.props.match.path}/activation`} component={TeamAccount}/>
                    </Switch>
                  </Grid.Column>
                </Grid.Row>
              </Grid>
            </div>
          </div>
        </div>
        </StripeProvider>
    )
  }
}

module.exports = TeamSettings;