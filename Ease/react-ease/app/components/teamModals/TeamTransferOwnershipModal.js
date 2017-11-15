var React = require('react');
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showTeamTransferOwnershipModal} from "../../actions/teamModalActions";
import {transferTeamOwnership} from "../../actions/userActions";
import {handleSemanticInput} from "../../utils/utils";
import {connect} from "react-redux";
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';


@connect((store)=>({
  team: store.teams[store.teamModals.teamTransferOwnershipModal.team_id],
  team_user_id: store.teamModals.teamTransferOwnershipModal.team_user_id
}))
class TeamTransferOwnershipModal extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      password : "",
      errorMessage: '',
      loading: false,
      user: this.props.team.team_users[this.props.team_user_id]
    };
  }
  handleInput = handleSemanticInput.bind(this);
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(transferTeamOwnership({
      team_id: this.props.team.id,
      team_user_id: this.state.user.id,
      password: this.state.password
    })).then(r => {
      this.setState({loading: false});
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    });
  };
  close = () => {
    this.props.dispatch(showTeamTransferOwnershipModal({active: false}));
  };
  render(){
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'Transfer team Ownership'}>
          <Form className="container" onSubmit={this.confirm} error={!!this.state.errorMessage.length}>
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
                loading={this.state.loading}
                disabled={!this.state.password.length}
                negative
                className="modal-button"
                content="CONFIRM TRANSFER"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = TeamTransferOwnershipModal;