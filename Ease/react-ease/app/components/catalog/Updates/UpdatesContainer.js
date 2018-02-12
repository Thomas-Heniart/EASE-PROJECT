import React from 'react';
import {connect} from "react-redux";
import {NewAppLabel} from "../../dashboard/utils";
import { Grid, Image, Icon, Container } from 'semantic-ui-react';
import {deleteUpdate, newAccountUpdateModal} from "../../../actions/catalogActions";
import {getLogo} from "../../../utils/api";

@connect(store => ({
  dashboard: store.dashboard,
  teams: store.teams,
  team_apps: store.team_apps,
  updates: store.catalog.updates
}))
class UpdatesContainer extends React.Component {
  getLogoAny = (url) => {
    getLogo({url: url}).then(response => {
      if (response !== '/resources/icons/link_app.png')
        return response;
      else
        return '';
    });
  };
  render() {
    const {
      title,
      websites,
    } = this.props;
    return (
      <Container fluid>
        <h3>
          {title}
        </h3>
        <Grid columns={4} className="logoCatalog">
          {this.props.updates.map((item) => {
            let website = {};
            websites.filter(site => {
              if (site.id === item.website_id)
                website = site;
              return site;
            });
            if (item.app_id !== -1) {
              website = this.props.dashboard.apps[item.app_id].website;
              website.app_name = this.props.dashboard.apps[item.app_id].name;
              if (this.props.dashboard.apps[item.app_id].sub_type === 'any')
                website.name = this.props.dashboard.apps[item.app_id].name
            }
            if (item.website_id === -1) {
              website = {
                url: item.url,
                logo: this.getLogoAny(item.url)
              };
            }
            return (
              <Grid.Column key={item.id} className="showSegment update">
                {website.logo && website.logo !== '' ?
                <Image src={website.logo} label={<NewAppLabel/>}/>
                :
                  <div className="logo">
                    <div className='div_wait_logo'>
                      <Icon name='wait'/>
                    </div>
                  </div>}
                <div className='wrap'>
                  <p>{website.name}</p>
                  {((item.app_id === -1 && item.team_card_id === -1)
                    || (item.team_card_id !== -1 && (this.props.team_apps[item.team_card_id].type !== "teamEnterpriseCard"
                      && this.props.team_apps[item.team_card_id].team_user_filler_id !== this.props.teams[item.team_id].my_team_user_id
                      && this.props.team_apps[item.team_card_id].team_user_filler_id !== -1))) &&
                  <span>New Account</span>}
                  {(item.app_id !== -1 &&
                    (Object.keys(this.props.dashboard.apps[item.app_id].account_information).length > 0
                      && this.props.dashboard.apps[item.app_id].account_information.login !== '')) &&
                  <span>Password update</span>}
                  {(item.app_id !== -1 &&
                    (Object.keys(this.props.dashboard.apps[item.app_id].account_information).length === 0
                      || this.props.dashboard.apps[item.app_id].account_information.login === '')
                    && (item.team_card_id !== -1 && (this.props.team_apps[item.team_card_id].type === "teamEnterpriseCard"
                      || this.props.team_apps[item.team_card_id].team_user_filler_id === this.props.teams[item.team_id].my_team_user_id
                      || this.props.team_apps[item.team_card_id].team_user_filler_id === -1))) &&
                  <span>Account update</span>}
                  {(item.team_card_id !== -1
                    && (this.props.team_apps[item.team_card_id].team_user_filler_id === this.props.teams[item.team_id].my_team_user_id
                    || this.props.team_apps[item.team_card_id].team_user_filler_id === -1)) &&
                  <span className='room'>#{this.props.teams[item.team_id].rooms[this.props.team_apps[item.team_card_id].channel_id].name}</span>}
                </div>
                <Icon name="trash" onClick={() => this.props.dispatch(deleteUpdate({id: item.id}))}/>
                <a onClick={() => newAccountUpdateModal(
                  this.props.dispatch,
                  website,
                  item.account_information
                )}>Manage now <Icon name="caret right"/></a>
              </Grid.Column>)
          })}
        </Grid>
      </Container>
    )
  }
}

export default UpdatesContainer;