import React from 'react';
import {connect} from "react-redux";
import {getLogo} from "../../../utils/api";
import {NewAppLabel} from "../../dashboard/utils";
import { Grid, Image, Icon, Container } from 'semantic-ui-react';
import {accountUpdateModal, newAccountUpdateModal, passwordUpdateModal, deleteUpdate} from "../../../actions/catalogActions";

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
      type: {},
      loading: false,
      websites: {}
    }
  }
  componentWillMount() {
    let stateWebsites = {};
    this.props.updates.map((item, idx) => {
      let website = {};
      this.props.websites.filter(site => {
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
        getLogo({url: item.url}).then(response => {
          website = {
            name: item.url,
            url: item.url,
            logo: response !== '/resources/icons/link_app.png' ? response : '',
            information: {
              login: {name: 'login', placeholder: "Login", priority: 0, type: "text"},
              password: {name: 'password', placeholder: "Password", priority: 1, type: "password"}
            }
          };
          stateWebsites[idx] = website;
          this.setState({websites: stateWebsites});
        });
      }
      stateWebsites[idx] = website;
    });
    this.setState({websites: stateWebsites});
  }
  openModal = ({item, website, account_information}) => {
    if (this.state.type[item.id] === 'account')
      accountUpdateModal(
        this.props.dispatch,
        website,
        item
      ).then(response => {
      });
    else if (this.state.type[item.id] === 'new')
      newAccountUpdateModal(
        this.props.dispatch,
        website,
        account_information
      ).then(response => {
      });
    else
      passwordUpdateModal(
        this.props.dispatch,
        website,
        item
      ).then(response => {
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
      title
    } = this.props;
    return (
      <Container fluid>
        <h3>
          {title}
        </h3>
        <Grid columns={4} className="logoCatalog">
          {this.props.updates.map((item, idx) => {
            const meId = item.team_id !== -1 ? this.props.teams[item.team_id].my_team_user_id : -1;
            const card = item.team_card_id !== -1 ? this.props.team_apps[item.team_card_id] : -1;
            const app = item.app_id !== -1 ? this.props.dashboard.apps[item.app_id] : -1;
            const website = this.state.websites[idx];
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
                  item: item,
                  website: website,
                  account_information: item.account_information})}>
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