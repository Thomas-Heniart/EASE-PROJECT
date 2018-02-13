import React from 'react';
import {connect} from "react-redux";
import {NewAppLabel} from "../../dashboard/utils";
import { Grid, Image, Icon, Container } from 'semantic-ui-react';
import {accountUpdateModal, newAccountUpdateModal, passwordUpdateModal, deleteUpdate} from "../../../actions/catalogActions";
import {getLogo} from "../../../utils/api";

@connect(store => ({
  dashboard: store.dashboard,
  teams: store.teams,
  team_apps: store.team_apps,
  updates: store.catalog.updates
}))
class UpdatesContainer extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      type: {}
    }
  }
  getLogoAny = (url) => {
    getLogo({url: url}).then(response => {
      if (response !== '/resources/icons/link_app.png')
        return response;
      else
        return '';
    });
  };
  getWebsite = ({websites, item, app}) => {
    let website = {};
    websites.filter(site => {
      if (site.id === item.website_id)
        website = site;
      return site;
    });
    if (item.app_id !== -1) {
      website = app.website;
      website.app_name = app.name;
      if (app.sub_type === 'any')
        website.name = app.name
    }
    if (item.website_id === -1) {
      website = {
        name: item.url,
        url: item.url,
        logo: this.getLogoAny(item.url),
        information: {
          login: {placeholder: "Login",priority:0,type:"text"},
          password: {placeholder:"Password",priority:1,type:"password"}
        }
      };
    }
    return website;
  };
  confirmPasswordUpdate = ({response, item, app, team}) => {
    if (item.team_card_id !== -1) {
      if (app.type === 'teamEnterpriseApp') {
        this.props.dispatch(teamEditEnterpriseCardReceiver({
          team_id: item.team_id,
          team_card_id: item.team_card_id,
          team_card_receiver_id: this.props.team_apps[item.team_card_id].receivers.filter(receiver => {
            return team.my_team_user_id === receiver.team_user_id
          })[0].id,
          account_information: response.account_information
        })).then(response => {
          this.props.dispatch(deleteUpdate({id: item.id}));
        });
      }
      else {
        this.props.dispatch(teamEditSingleCardCredentials({
          team_card: this.props.team_apps[item.team_card_id],
          account_information: response.account_information
        })).then(response => {
          this.props.dispatch(deleteUpdate({id: item.id}));
        });
      }
    }
    else {
      this.props.dispatch(editAppCredentials({
        app: app,
        account_information: response.account_information
      })).then(() => {
        this.props.dispatch(deleteUpdate({id: item.id}));
      });
    }
  };
  confirmAccountUpdate = ({response, item, app, team, account_information}) => {
    if (response.check === 'Simple') {
      if (item.team_card_id !== -1) {
        if (app.type === 'teamEnterpriseApp') {
          this.props.dispatch(teamEditEnterpriseCardReceiver({
            team_id: item.team_id,
            team_card_id: item.team_card_id,
            team_card_receiver_id: this.props.team_apps[item.team_card_id].receivers.filter(item => {
              return team.my_team_user_id === item.team_user_id
            })[0].id,
            account_information: response.account_information
          })).then(response => {
            this.props.dispatch(deleteUpdate({id: item.id}));
          });
        }
        else {
          this.props.dispatch(teamEditSingleCardCredentials({
            team_card: this.props.team_apps[item.team_card_id],
            account_information: response.account_information
          })).then(response => {
            this.props.dispatch(deleteUpdate({id: item.id}));
          });
        }
      }
      else {
        this.props.dispatch(editAppCredentials({
          app: app,
          account_information: response.account_information
        })).then(response => {
          this.props.dispatch(deleteUpdate({id: item.id}));
        });
      }
    }
    else
      newAccountUpdateModal(
        this.props.dispatch,
        website,
        account_information
      );
  };
  openModal = ({app, item, website, account_information, team, room}) => {
    if (this.state.type[item.id] === 'account')
      accountUpdateModal(
        this.props.dispatch,
        website,
        account_information,
        team,
        room,
      ).then(response => {
        response && this.confirmAccountUpdate({response, item, app, team, account_information})
      });
    else if (this.state.type[item.id] === 'new')
      newAccountUpdateModal(
        this.props.dispatch,
        website,
        account_information
      ).then(response => {
        // ce que tu dois faire quand la promesse est résolue
      });
    else
      passwordUpdateModal(
        this.props.dispatch,
        website,
        account_information,
        team,
        room,
      ).then(response => {
        response && this.confirmPasswordUpdate({response, item, app, team});
      });
  };
  typeUpdate = (item, card, app, meId) => {
    if ((item.app_id === -1 && item.team_card_id === -1) || (item.team_card_id !== -1 && (card.type !== "teamEnterpriseCard"
      && card.team_user_filler_id !== meId && card.team_user_filler_id !== -1))) {
      this.state.type[item.id] = 'new';
      return <span>New Account</span>;
    }
    else if (item.app_id !== -1 && (Object.keys(app.account_information).length > 0 && app.account_information.login !== '')) {
      this.state.type[item.id] = 'password';
      return <span>Password update</span>;
    }
    else if (item.app_id !== -1 &&
      (Object.keys(app.account_information).length === 0 || app.account_information.login === '')
      && (item.team_card_id !== -1 && (card.type === "teamEnterpriseCard"
        || card.team_user_filler_id === meId || card.team_user_filler_id === -1))) {
      this.state.type[item.id] = 'account';
      return <span>Account update</span>;
    }
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
            const meId = item.team_id !== -1 ? this.props.teams[item.team_id].my_team_user_id : -1;
            const card = item.team_card_id !== -1 ? this.props.team_apps[item.team_card_id] : -1;
            const app = item.app_id !== -1 ? this.props.dashboard.apps[item.app_id] : -1;
            const website = this.getWebsite({websites, item, app});
            return (
              <Grid.Column key={item.id} className="showSegment update">
                {website.logo && website.logo !== '' ?
                <Image src={website.logo} label={<NewAppLabel/>}/>
                :
                  <div className="logo">
                    <div className='div_wait_logo'>
                      <NewAppLabel/>
                      <Icon name='wait'/>
                    </div>
                  </div>}
                <div className='wrap'>
                  <p>{website.name}</p>
                  {this.typeUpdate(item, card, app, meId)}
                  {(item.team_card_id !== -1
                    && (card.type === "teamEnterpriseCard"
                      || (card.team_user_filler_id === meId || card.team_user_filler_id === -1))) &&
                  <span className='room'>#{this.props.teams[item.team_id].rooms[card.channel_id].name}</span>}
                </div>
                <Icon name="trash" onClick={() => this.props.dispatch(deleteUpdate({id: item.id}))}/>
                <a onClick={() => this.openModal({
                  app: app,
                  item: item,
                  website: website,
                  account_information: item.account_information,
                  team: item.team_id !== -1 ? this.props.teams[item.team_id] : -1,
                  room: item.team_id !== -1 ? this.props.teams[item.team_id].rooms[card.channel_id] : -1})}>
                  Manage now <Icon name="caret right"/>
                </a>
              </Grid.Column>)
          })}
        </Grid>
      </Container>
    )
  }
}

export default UpdatesContainer;