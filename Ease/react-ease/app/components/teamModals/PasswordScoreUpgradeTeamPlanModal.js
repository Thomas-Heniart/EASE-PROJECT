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
              Sorry, to get a precise view on which passwords of your team are vulnerable, or found in public data breaches, try our               <a className="simple_link" href="/pricing" target="_blank">Pro plan</a>.
            </Form.Field>
            <Form.Field style={{color: "#949EB7"}}>
              1 month free, no credit card needed.
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