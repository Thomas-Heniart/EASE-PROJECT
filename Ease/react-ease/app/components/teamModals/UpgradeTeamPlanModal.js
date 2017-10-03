import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import {upgradePlan} from "../../actions/teamActions";
import {connect} from "react-redux";
import post_api from "../../utils/post_api";
import {proFeaturesDesc, proFeatures} from "../../utils/teamPlans";
import {selectItemFromListById, isOwner} from "../../utils/helperFunctions";

@connect(store => ({
  team_id: store.team.id,
  feature_id: store.teamModals.upgradeTeamPlanModal.feature_id,
  myId: store.team.myTeamUserId,
  users: store.users.users
}))
class UpgradeTeamPlanModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      show_more : false,
      loading: false,
      errorMesage: ''
    }
  }
  showMore = (state) => {
    this.setState({show_more: state});
  };
  confirm = (e) => {
    e.preventDefault();
    const me = selectItemFromListById(this.props.users, this.props.myId);
    const meOwner = isOwner(me.role);
    this.setState({loading: true, errorMesage: ''});
    if (meOwner) {
      this.props.dispatch(upgradePlan({
        team_id: this.props.team_id,
        plan_id: 1
      })).then(response => {
        this.setState({loading: false});
        this.props.dispatch(showUpgradeTeamPlanModal(false));
      }).catch(err => {
        this.setState({loading: false, errorMesage: err});
      });
    } else {
      post_api.teams.askOwnerToUpgrade({team_id: this.props.team_id}).then(response => {
        this.setState({loading: false});
        this.props.dispatch(showUpgradeTeamPlanModal(false));
      });
    }
  };
  render(){
    const feature_id = this.props.feature_id;
    const me = selectItemFromListById(this.props.users, this.props.myId);
    const meOwner = isOwner(me.role);
    const teamOwner = this.props.users.find(item => (isOwner(item.role)));
    const featuresList = proFeatures.map((item, idx) => {
      if (!this.state.show_more && idx > 2)
        return null;
      return (
          <List.Item key={idx}>
            <List.Icon name="checkmark" color="green"/>
            <List.Content>
              {item}
            </List.Content>
          </List.Item>
      )
    });
    return (
        <SimpleModalTemplate
            onClose={e => {this.props.dispatch(showUpgradeTeamPlanModal(false))}}
            headerContent={'Try Pro now!'}>
          <Form class="container" error={this.state.errorMesage.length > 0} onSubmit={this.confirm} id="upgrade_team_plan_modal">
            <Form.Field>
              Your current plan (Basic) doesn’t enable {proFeaturesDesc[feature_id]}.
            </Form.Field>
            <Form.Field>
              <h5>
                Pro includes your current features and:
              </h5>
              <List class="features">
                {featuresList}
              </List>
              {!this.state.show_more &&
              <button onClick={this.showMore.bind(null, true)} class="button-unstyle inline-text-button" type="button">Show all features</button>}
            </Form.Field>
            <Form.Field>
              {meOwner ?
                  'After trial Pro is billed 3,99€ per month per active user, but for now it’s free 1 month and no credit required' :
                  `After trial Pro is billed 3,99€ per month per active user. Your team owner ${teamOwner.username}, is the only person able to take decision to upgrade.Want to send a request ?`
              }
            </Form.Field>
            <Message error content={this.state.errorMesage}/>
            <Button
                attached='bottom'
                type="submit"
                positive
                loading={this.state.loading}
                onClick={this.confirm}
                class="modal-button uppercase"
                content={meOwner ? 'try PRO 30 days for free' : `Ask ${teamOwner.username} to upgrade`}/>
          </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = UpgradeTeamPlanModal;