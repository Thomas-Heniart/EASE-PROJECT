var React = require('react');
var classnames = require('classnames');
import {showTeamTransferOwnershipModal} from "../../actions/teamModalActions";
import {transferTeamOwnership} from "../../actions/userActions";
import {connect} from "react-redux";
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';


@connect((store)=>{
  return {
    team: store.team,
    modal: store.teamModals.teamTransferOwnershipModal
  };
})
class TeamTransferOwnershipModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      password : "",
      errorMessage: '',
      loading: false
    };
    this.confirmModal = this.confirmModal.bind(this);
    this.handleInput = this.handleInput.bind(this);
  }
  handleInput(e){
    this.setState({[e.target.name]: e.target.value});
  }
  confirmModal(){
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(transferTeamOwnership(this.state.password, this.props.modal.user.id)).then(r => {
      this.setState({loading: false});
      this.props.dispatch(showTeamTransferOwnershipModal(false));
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  }
  render(){
    const user = this.props.modal.user;
    const myId = this.props.team.myTeamUserId;

    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showTeamTransferOwnershipModal(false))}}></div>
          <div class="ease_popup ease_team_popup" id="modal_verify_team_user">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showTeamTransferOwnershipModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <Header as="h3" attached="top">
              Transfer team Ownership
            </Header>
            <Form className="container" onSubmit={this.confirmModal} error={this.state.errorMessage.length > 0}>
              <Form.Field>
                <p>Transferring the ownership of a team in a one-way street. You won’t be able to undo this action.</p>
                <p>Enter your Ease.space password to confirm the transfer.</p>
              </Form.Field>
              <Form.Input
                  type="password"
                  name="password"
                  placeholder="Password"
                  onChange={this.handleInput}
                  value={this.state.password}
                  id="password"/>
              <Message error content={this.state.errorMessage}/>
              <Button
                      onClick={this.confirmModal}
                      loading={this.state.loading}
                      disabled={this.state.password.length === 0}
                      attached='bottom' negative
                      className="modal-button"
                      content="CONFIRM TRANSFER"/>
            </Form>
          </div>
        </div>
    )
  }
}

module.exports = TeamTransferOwnershipModal;