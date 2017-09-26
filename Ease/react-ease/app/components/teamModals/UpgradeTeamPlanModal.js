import React, {Component} from "react";
import SimpleModalTemplate from "../common/SimpleModalTemplate";
import { Header, Label,List, Search,SearchResult, Container, Divider, Icon, Transition, TextArea, Segment, Checkbox, Form, Input, Select, Dropdown, Button, Message } from 'semantic-ui-react';
import {showUpgradeTeamPlanModal} from "../../actions/teamModalActions";
import {connect} from "react-redux";
import {selectItemFromListById, isOwner} from "../../utils/helperFunctions";

const features = [
    'Unlimited Rooms',
    'Accesses sharing with showing or hiding passwords',
    'SaaS tools sharing with customization of credentials for each user',
    'Automatic accesses revocation set up',
    'Unlimited Admins and 1 Owner',
    'Prioritary website integration requests',
    'Prioritary customer support'
];

@connect(store => ({
  featureDesc: store.teamModals.upgradeTeamPlanModal.featureDescription,
  myId: store.team.myTeamUserId,
  users: store.users.users
}))
class UpgradeTeamPlanModal extends Component {
  constructor(props){
    super(props);
  }
  confirm = (e) => {
    e.preventDefault();
  };
  render(){
    const featureDesc = this.props.featureDesc;
    const me = selectItemFromListById(this.props.users, this.props.myId);
    const isOwner = isOwner(me.role);
    const teamOwner = this.props.users.find(item => (isOwner(item.role)));
    const featuresList = features.map((item, idx) => {
      return <List.Item icon="checkmark" content={item} key={idx}/>
    });
    return (
        <SimpleModalTemplate
        onClose={showUpgradeTeamPlanModal(false)}
        headerContent={'Upgrade to Pro!'}>
        <Form class="container" onSubmit={this.confirm}>
          <Form.Field>
            Your current plan (Basic) doesn’t enable {featureDesc}.
          </Form.Field>
          <Form.Field>
            <Header as="h5">
              Pro includes your current features and:
            </Header>
            <List>
              {featuresList}
            </List>
          </Form.Field>
          <Form.Field>
            {isOwner ?
            'After trial Pro is billed 3,99€ per month per active user, but for now it’s free 1 month and no credit required' :
            `After trial Pro is billed 3,99€ per month per active user. Your team owner ${teamOwner.username}, is the only person able to take decision to upgrade.Want to send a request ?`
            }
          </Form.Field>
          <Button
              attached='bottom'
              type="submit"
              positive
              onClick={this.confirm}
              className="modal-button"
              content={isOwner ? 'try PRO 30 days for free' : `Ask ${teamOwner.username} to upgrade`}/>
        </Form>
        </SimpleModalTemplate>
    )
  }
}

module.exports = UpgradeTeamPlanModal;