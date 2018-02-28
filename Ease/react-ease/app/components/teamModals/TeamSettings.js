import React, {Component, Fragment} from 'react';
import {editTeamName} from '../../actions/teamActions';
import { Progress, Header, Container, Menu, Segment, Comment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';
import {StripeProvider} from 'react-stripe-elements';
import {connect} from "react-redux";
import {handleSemanticInput, blacklistInviteTeamUsersEmails} from "../../utils/utils";
import {isOwner} from "../../utils/helperFunctions";
import {fetchTeamPaymentInformation, teamInviteFriends, teamUpdateBillingInformation, unsubscribe} from "../../actions/teamActions";
import countryValues from "../../utils/countrySelectList";
import {withRouter, Switch, Route, NavLink} from "react-router-dom";
import {inviteFriend} from "../../actions/teamActions";
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
    }).catch(err => {
      this.setState({loading: false});
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
                Your Team name appears in the Team Space and invitation emails, but is not displayed publicly.
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
              Credit card information
            </Header>
            <p>Our billing is fair and transparent: you won’t be billed until your free trial ends :)</p>
            <p>No commitment: unsubscribe whenever you want, we bill monthly.</p>
            <p>Pro is 59€/month before VAT.</p>
            <Header as="h4">
              {!!card ? 'Credit card information' : 'There is no credit card set up yet'}
            </Header>
            {!!card && !this.state.modifying &&
            <CreditCardPreview card={card}/>}
            {!this.state.modifying &&
            <div class="overflow-hidden">
              <Button primary size="mini" content={!!card ? 'Replace this card' : 'Add credit card'} floated="right" onClick={this.setModifying.bind(null, true)}/>
            </div>}
            {this.state.modifying &&
            <MyStoreCheckout
                team_id={team_id}
                cancel={this.setModifying.bind(null, false)}
                validate={this.cardTokenCallback}/>}
          </Segment>
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
              Add billing information
            </Header>
            <p>
              Do you want to have some company information displayed on your monthly bill?
            </p>
            {!payment.loading && card === null &&
            <Message color="red">
              Please&nbsp;
              <NavLink style={{color:'inherit', textDecoration: 'underline'}}to={`/teams/${this.props.match.params.teamId}/${this.props.match.params.itemId}/settings/payment`}>
                set up your credit card
              </NavLink>&nbsp;
              before adding billing information.
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
  };
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
            Team activation
          </Header>
          <Segment size="mini">
            <Header as="h4">
              Team Account
            </Header>
            <div style={{marginBottom:'1rem'}}>
              <p>Your team is now active.</p>
              <p>We bill each 30 or 31 days. So if you started Pro on a 12th, we will bill you each month on the 12th.</p>
              <p>You can unsubscribe at anytime, if you started Pro on a 12th and you unsubscribe on a 28th, you won’t be billed the following month.</p>
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
                By unsubscribing, you will loose the team passwords and the team space permanently.
              </Form.Field>
              <Form.Field>
                <Checkbox
                    className="mini"
                    checked={this.state.checked}
                    onClick={this.check}
                    label="I accept the condition of unsubscription"/>
              </Form.Field>
              <Form.Group>
                <Form.Input
                    required
                    width={8}
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
                <Button type="button" floated="right" size="mini" content='Cancel' onClick={this.setModifying.bind(null, false)}/>
              </Form.Field>
            </Form>}
          </Segment>
        </div>
    )
  }
}

function SettingsMenu(props){
  const {team} = props;
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
        {team.plan_id === 0 &&
        <Menu.Item as={NavLink} to={`${props.match.url}/referral`}>
          <Header as="h4">
            <img style={{height: '19px', width: 'auto'}} src="/resources/images/team_settings_referral_header.png"/>
          </Header>
          <span/>
        </Menu.Item>}
      </Menu>
  )
}


@connect(store => ({
  teams: store.teams
}))
class ReferralSection extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      modifying: false,
      email: '',
      errorMessage: ''
    }
  }
  handleInput = handleSemanticInput.bind(this);
  setModifying = (state) => {
    this.setState({modifying: state});
  };
  submit = (e) => {
    e.preventDefault();
    const team_id = this.props.match.params.teamId;
    const email = this.state.email;

    this.setState({errorMessage: ''});
    if (blacklistInviteTeamUsersEmails.indexOf(email.split('@')[1]) !== -1){
      this.setState({errorMessage: 'Please use a company email!'});
      return;
    }
    this.setState({loading: true});
    this.props.dispatch(inviteFriend({
      team_id: team_id,
      email: email
    })).then(response => {
      this.setState({loading: false, email: ''});
      this.inputRef.focus();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
      this.inputRef.focus();
    });
  };
  render(){
    const team = this.props.teams[this.props.match.params.teamId];
    const maxInvitations = 15 + team.extra_members;
    const currentInvitations = Object.keys(team.team_users).reduce((value, team_user_id) => {
      const team_user = team.team_users[team_user_id];
      if (team_user.invitation_sent)
        return ++value;
      return value;
    }, 0);
    const availableInvitations = maxInvitations - currentInvitations;

    return (
        <div class="team_settings_section" id="team_referral_section">
          <Header as="h3">
            <img style={{height: '24px', width: 'auto'}}src="/resources/images/team_settings_referral_header.png"/>
          </Header>
          <Segment size="mini">
            <Header as="h4">
              Team {team.name}:
            </Header>
            <Form size="mini" onSubmit={this.submit} error={!!this.state.errorMessage.length}>
              <Form.Field class="display_flex align_items_center">
                <Progress
                    total={maxInvitations}
                    value={currentInvitations}/>
                <strong>{availableInvitations} seats available!!</strong>
              </Form.Field>
              <Form.Field class="display_flex flex_direction_column">
                <strong style={{marginBottom: '11px'}}>
                  Earn more seats:
                </strong>
                <span>
                  It is simple, 1 friend referred = 1 extra seat for your team. You can go up to 30 persons in your team.
                </span>
              </Form.Field>
              {this.state.modifying ?
                  <React.Fragment>
                    <Form.Field
                        width={10}>
                      <Input
                          name="email"
                          ref={(ref)=>{this.inputRef = ref}}
                          autoFocus
                          type="email"
                          required
                          placeholder="friend@company.com"
                          onChange={this.handleInput}
                          value={this.state.email}/>
                    </Form.Field>
                    {!!this.state.errorMessage.length &&
                    <Form.Field>
                      <Message error content={this.state.errorMessage}/>
                    </Form.Field>}
                    <Form.Field class="display_flex align_items_center" style={{justifyContent:'flex-end'}}>
                      <Button
                          loading={this.state.loading}
                          disabled={this.state.loading}
                          primary
                          size="mini"
                          content="Invite friends"/>
                    </Form.Field>
                  </React.Fragment> :
                  <Form.Field class="display_flex align_items_center" style={{justifyContent:'flex-end'}}>
                    <strong style={{marginRight: '20px'}}>{team.extra_members} friends referred</strong>
                    <Button
                        disabled={team.extra_members === 15}
                        type="button"
                        primary
                        size="mini"
                        content="Invite friends"
                        onClick={this.setModifying.bind(null, true)}/>
                  </Form.Field>}
            </Form>
          </Segment>
        </div>
    )
  }
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
  componentWillMount = () => {
    const team_id = this.props.match.params.teamId;
    const team = this.props.teams[team_id];

    if (!isOwner(team.team_users[team.my_team_user_id].role))
      this.close();
  };
  componentDidMount(){
    this.props.dispatch(fetchTeamPaymentInformation({team_id: this.props.match.params.teamId}));
    if (this.props.match.isExact)
      this.props.history.replace(`${this.props.match.url}/information`);
  };
  render() {
    const team = this.props.teams[this.props.match.params.teamId];
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
                      <SettingsMenu
                          team={team}
                          teamName={this.props.teams[this.props.match.params.teamId].name}
                          match={this.props.match}/>
                    </Grid.Column>
                    <Grid.Column width={11}>
                      <Switch>
                        <Route exact path={`${this.props.match.path}/`} component={TeamInformations}/>
                        <Route path={`${this.props.match.path}/information`} component={TeamInformations}/>
                        <Route path={`${this.props.match.path}/payment`} component={PaymentMethod}/>
                        <Route path={`${this.props.match.path}/billing`} component={BillingInformationWithRouter}/>
                        <Route path={`${this.props.match.path}/activation`} component={TeamAccount}/>
                        {team.plan_id === 0 &&
                        <Route path={`${this.props.match.path}/referral`} component={ReferralSection}/>}
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