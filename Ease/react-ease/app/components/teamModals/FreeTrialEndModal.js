import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {showFreeTrialEndModal} from "../../actions/teamModalActions";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {connect} from "react-redux";

@connect(store => ({
  team_id: store.team.id
}))
class FreeTrialEndModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  confirm = (e) => {
    e.preventDefault();
  };
  render(){
    return (
        <SimpleModalTemplate
            onClose={e => {this.props.dispatch(showTeamJoinMultiAppModal(false))}}
            headerContent={'Free trial ended'}>
          <Form class="container" onSubmit={this.confirm} error={this.state.errorMessage.length > 0}>
            <Message error content={this.state.errorMessage}/>
            <Button
                attached='bottom'
                type="submit"
                loading={this.state.loading}
                positive
                onClick={this.confirm}
                className="modal-button"
                content="CONFIRM"/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = FreeTrialEndModal;