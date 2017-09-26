var React = require('react');
var classnames = require('classnames');
import {common} from "../../utils/post_api";
import {showRequestWebsiteModal} from "../../actions/teamModalActions";
import {connect} from "react-redux";
import {isUrl} from "../../utils/utils";
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';

function Informations(props){
  return (
      <Container className="infoColor">
        Ease.space proposes to integrate any website you would like for free.
        <br/><br/>
        In order for us to do the technical work of integrating a website, we need to access it with an account, this is why we request credentials.
        <br/><br/>
        The credentials you would provide us, won’t be stored anywhere and will be cyphered as soon as you type them. They will be cyphered with a key wich is unvailable from internet, which means, only the developer in charge of your website integration, will access them. This person stands to an absolute commitment to the non-disclosure agreement of our company Ease.space.
        <br/><br/>
        As soon as the process of integration is completed, our access to the credentials you provided us, will be disabled.
        <br/><br/>
        If you want to know more information about this process, please call our CEO, Benjamin Prigent on +33 6 26 97 88 97.
        <br/><br/>
        <Button
            onClick={props.validate}
            attached='bottom' positive className="modal-button" type="submit" content="OK"/>
      </Container>
  )
}

function Step1(props){
  return (
      <Form className="container">
        <Form.Field>
          <label>
            Website URL
          </label>
          <Input
              name="url"
              value={props.url}
              onChange={props.handleInput}
              placeholder="Paste website URL"/>
          <Label pointing class="fluid text-center" content={'You must paste a website URL.'}/>
        </Form.Field>
        <Form.Checkbox
            toggle
            checked={props.private}
            name="private"
            onChange={props.handleCheckbox}
            label="I wish this website to be private and do not appear in the public catalog of Apps"/>
        <Form.Field className="infoColor">
          In order to add this website to my apps I authorize Ease.space to use my credentials for a temporarily period of time (72 hours).
          &nbsp;<span class="inline-text-button" onClick={props.viewInfo}>More info</span>
        </Form.Field>
        <Form.Checkbox
            name="authorization"
            checked={props.authorization}
            onChange={props.handleCheckbox}
            label="Yes, I give my authorization"/>
        <Button
            disabled={!props.authorization || !isUrl(props.url)}
            onClick={props.validate}
            attached='bottom' positive className="modal-button" type="submit" content="NEXT"/>
      </Form>
  )
}

function Step2(props){
  return (
      <Form className="container" error={props.errorMessage.length > 0}>
        <Form.Field className="infoColor">
          You can provide us whether your own credentials or test ones made just for Ease.space.
        </Form.Field>
        <Form.Input
            label="Login"
            name="login"
            type="text"
            value={props.login}
            onChange={props.handleInput}
            placeholder="Login"/>
        <Form.Input
            label="Password"
            name="password"
            type="password"
            value={props.password}
            onChange={props.handleInput}
            placeholder="Password"/>
        <Message error content={props.errorMessage}/>
        <Button onClick={props.validate}
                loading={props.loading}
                disabled={props.login.length === 0 || props.password.length === 0}
                attached='bottom' positive
                className="modal-button"
                type="submit"
                content="REQUEST THIS APP!"/>
      </Form>
  )
}

@connect(store => ({
  modal: store.teamModals.requestWebsiteModal,
  team_id: store.team.id
}))
class RequestWebsiteModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      url: '',
      private: false,
      authorization: false,
      login: '',
      password: '',
      view: 0,
      errorMessage: '',
      loading: false
    };
    this.handleInput = this.handleInput.bind(this);
    this.handleCheckbox = this.handleCheckbox.bind(this);
    this.validateModal = this.validateModal.bind(this);
    this.closeModal = this.closeModal.bind(this);
    this.setStep = this.setStep.bind(this);
  }
  handleInput(e, {name, value}){
    this.setState({[name]: value});
  }
  handleCheckbox(e, {name}){
    this.setState({[name]: !this.state[name]});
  }
  setStep(step){
    this.setState({view: step});
  }
  closeModal(){
    this.props.modal.reject();
    this.props.dispatch(showRequestWebsiteModal(false));
  }
  validateModal(){
    this.setState({loading: true, errorMessage: ''});
    common.requestWebsite({
      team_id: this.props.team_id,
      url: this.state.url,
      is_public: !this.state.private,
      login: this.state.login,
      password: this.state.password
    }).then(r => {
      this.props.modal.resolve(r);
      this.props.dispatch(showRequestWebsiteModal(false));
    }).catch(err => {
      this.setState({loading: false});
      this.setState({errorMessage: err});
    });
  }
  render(){
    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={this.closeModal}/>
          <div class="ease_popup ease_team_popup" id="modal_team_request_website">
            <button class="button-unstyle action_button close_button" onClick={this.closeModal}>
              <i class="fa fa-times"/>
            </button>
            <Header as="h3" attached="top">
              Request a website
            </Header>
            {this.state.view === 0 &&
            <Step1
                url={this.state.url}
                private={this.state.private}
                authorization={this.state.authorization}
                handleInput={this.handleInput}
                handleCheckbox={this.handleCheckbox}
                validate={this.setStep.bind(null, 1)}
                viewInfo={this.setStep.bind(null, 2)}/>}
            {this.state.view === 1 &&
            <Step2
                handleInput={this.handleInput}
                login={this.state.login}
                password={this.state.password}
                loading={this.state.loading}
                errorMessage={this.state.errorMessage}
                validate={this.validateModal}/>}
            {this.state.view === 2 &&
            <Informations validate={this.setStep.bind(null, 0)}/>}
          </div>
        </div>
    )
  }
}

module.exports = RequestWebsiteModal;