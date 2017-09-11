import {FormInput} from "../common/FormComponents";

var React = require('react');
var classnames = require('classnames');
import {showReactivateTeamUserModal} from "../../actions/teamModalActions";
import {reactivateTeamUser} from "../../actions/userActions";
import {connect} from "react-redux";
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';

@connect((store)=>{
  return {
    modal: store.teamModals.reactivateTeamUserModal
  };
})
class ReactivateTeamUserModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    };
  }
  confirmModal = (e) => {
    e.preventDefault();
    this.setState({errorMessage: '', loading: true});
    this.props.dispatch(reactivateTeamUser({team_user_id: this.props.modal.user.id})).then(response => {
      this.props.dispatch(showReactivateTeamUserModal(false));
    }).catch(err => {
      this.setState({errorMessage:err, loading:false});
    });
  };
  render(){
    const user = this.props.modal.user;

    return (
        <div class="popupHandler myshow">
          <div class="popover_mask" onClick={e => {this.props.dispatch(showReactivateTeamUserModal(false))}}/>
          <div class="ease_popup ease_team_popup" id="modal_reactivate_team_user">
            <button class="button-unstyle action_button close_button" onClick={e => {this.props.dispatch(showReactivateTeamUserModal(false))}}>
              <i class="fa fa-times"/>
            </button>
            <Header as="h3" attached="top" className="first_word_capitalize">
              {user.username}'s password has been lost
            </Header>
            <Form className="container" onSubmit={this.confirmModal} error={this.state.errorMessage.length > 0}>
              <Form.Field>
                <p>
                  <strong class="first_word_capitalize">{user.username}</strong>’s access to company apps has been blocked. For safety reasons, you need to validate <strong>{user.username}</strong>’s access again.
                </p>
                <p>
                  We advise you to call or meet with <strong>{user.username}</strong> to make sure the password renewal is authentic.
                </p>
                <p>
                  If <strong>{user.username}</strong>’s password renewal request does not seem authentic, please <a onClick={e => {this.props.dispatch(showReactivateTeamUserModal(false))}}>click here</a> to remain the account blocked.
                </p>
              </Form.Field>
              <Message error content={this.state.errorMessage}/>
              <Button
                      loading={this.state.loading}
                      attached='bottom'
                      onClick={this.confirmModal}
                      positive
                      className="modal-button"
                      type="submit"
                      content="OK, I GIVE THE ACESS"/>
            </Form>
          </div>
        </div>
    )
  }
}

module.exports = ReactivateTeamUserModal;