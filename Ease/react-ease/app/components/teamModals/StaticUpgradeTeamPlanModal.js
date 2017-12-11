import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import {Button, Form, List, Message} from 'semantic-ui-react';
import {upgradePlan} from "../../actions/teamActions";
import {connect} from "react-redux";
import post_api from "../../utils/post_api";
import {proFeatures} from "../../utils/teamPlans";
import {withRouter} from "react-router-dom";
import {isOwner, selectItemFromListById} from "../../utils/helperFunctions";

@connect(store => ({
  teams: store.teams
}))
class StaticUpgradeTeamPlanModal extends Component {
  constructor(props){
    super(props);
    this.state = {
      show_more : false,
      loading: false,
      errorMessage: ''
    }
  }
  showMore = (state) => {
    this.setState({show_more: state});
  };
  close = () => {
    this.props.history.push(`/teams/${this.props.match.params.teamId}/${this.props.match.params.itemId}`);
  };
  confirm = (e) => {
    e.preventDefault();
    const team = this.props.teams[this.props.match.params.teamId];
    const me = team.team_users[team.my_team_user_id];
    const meOwner = isOwner(me.role);
    this.setState({loading: true, errorMessage: ''});
    if (meOwner) {
      this.props.dispatch(upgradePlan({
        team_id: team.id,
        plan_id: 1
      })).then(response => {
        this.setState({loading: false});
          this.close();
      }).catch(err => {
        this.setState({loading: false, errorMessage: err});
      });
    } else {
      post_api.teams.askOwnerToUpgrade({team_id: team.id}).then(response => {
        this.setState({loading: false});
          this.close();
      });
    }
  };
  render(){
    const team = this.props.teams[this.props.match.params.teamId];
    const me = team.team_users[team.my_team_user_id];
    const meOwner = isOwner(me.role);
    const teamOwner = Object.keys(team.team_users).map(id => (team.team_users[id])).find(item => (isOwner(item.role)));
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
            onClose={this.close}
            headerContent={'Try Pro now!'}>
          <Form class="container" error={this.state.errorMessage.length > 0} onSubmit={this.confirm} id="upgrade_team_plan_modal">
              <Form.Field>
                  One of your team members would like to access paying features.
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
            <Message error content={this.state.errorMessage}/>
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

module.exports = withRouter(StaticUpgradeTeamPlanModal);