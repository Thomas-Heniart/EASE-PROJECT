import React, {Fragment, Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {upgradePlan} from "../../actions/teamActions";
import {showPasswordScoreUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import {connect} from "react-redux";

@connect(store => ({
  team_id: store.modals.passwordScoreUpgradeTeamPlan.team_id
}))
class PasswordScoreUpgradeTeamPlanModal extends Component{
  constructor(props){
    super(props);
    this.state = {
      loading: false,
      errorMessage: ''
    }
  }
  close = () => {
    this.props.dispatch(showPasswordScoreUpgradeTeamPlanModal({
      active: false
    }));
  };
  confirm = (e) => {
    e.preventDefault();
    this.setState({loading: true, errorMessage: ''});
    this.props.dispatch(upgradePlan({
      team_id: this.props.team_id,
      plan_id: 1
    })).then(response => {
      this.setState({loading: false});
      this.close();
    }).catch(err => {
      this.setState({loading: false, errorMessage: err});
    })
  };
  render(){
    return (
        <SimpleModalTemplate
            onClose={this.close}
            headerContent={'How to know which passswords are vulnerable in your team'}>
          <Form class="container"
                onSubmit={this.confirm}
                error={!!this.state.errorMessage.length}>
            <Form.Field style={{color: "#949EB7"}}>
              Sorry, you can't check which passwords are vulnerable in your team. To get a precise view on which passwords are too weak or found in public data breaches, try the <a class="simple_link" href="/pricing" target="_blank">Pro plan</a>. It will tell you and your team members which passwords to change.
            </Form.Field>
            <Form.Field style={{color: "#949EB7"}}>
              No credit card needed for 1 month trial. After, Pro is billed 59€ monthly.
            </Form.Field>
            <Message error content={this.state.errorMessage}/>
            <Button
                type="submit"
                positive
                loading={this.state.loading}
                class="modal-button uppercase"
                content={'try PRO 30 days for free'}/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

export default PasswordScoreUpgradeTeamPlanModal;