import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {Button, Form, Message} from 'semantic-ui-react';
import {showDepartureDateEndModal} from "../../actions/teamModalActions";
import {deleteTeamUser, editTeamUserDepartureDate} from "../../actions/userActions";
import {basicDateFormat} from "../../utils/utils";
import {connect} from "react-redux";

@connect(store => ({
  user: store.teamModals.departureDateEndModal.user
}))
class DepartureDateEndModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  unfreeze = () => {
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(editTeamUserDepartureDate(this.props.user.id, 0)).then(() => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(deleteTeamUser(this.props.user.id)).then(() => {
      this.setState({loading: false});
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  render() {
    const user = this.props.user;
    return (
        <SimpleModalTemplate
            onClose={e => {
              this.props.dispatch(showDepartureDateEndModal(false))
            }}
            headerContent={`${user.username}'s account is frozen`}>
          <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={this.confirm}>
            <Form.Field>
              <strong class="capitalize">{user.username}</strong>’s account has been frozen because the
              departure date was set on {basicDateFormat(user.departure_date)}. You can now decide to unfreeze the account or
              confirm the departure.
            </Form.Field>
            <Form.Field style={{textAlign: 'right'}}>
              <button class="button-unstyle inline-text-button" type="button" onClick={this.unfreeze}>Unfreeze Account</button>
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                attached='bottom'
                type="submit"
                positive
                loading={this.state.loading}
                onClick={this.confirm}
                class="modal-button uppercase"
                content={'CONFIRM DEPARTURE'}/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = DepartureDateEndModal;