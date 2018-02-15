import React from 'react';
import {connect} from "react-redux";
import {getLogo} from "../../../utils/api";
import {NewAppLabel} from "../../dashboard/utils";
import { Grid, Image, Icon, Container, Loader } from 'semantic-ui-react';
import {accountUpdateModal, newAccountUpdateModal, passwordUpdateModal, deleteUpdate} from "../../../actions/catalogActions";

@connect(store => ({
  dashboard: store.dashboard,
  teams: store.teams,
  team_apps: store.team_apps,
  updates: store.catalog.updates,
  sso_list: store.catalog.sso_list
}))
class UpdatesContainer extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      type: {},
      loadingDelete: {},
      websites: {}
    }
  }
  componentWillMount() {
    let stateWebsites = {};
    let loading = {};
    this.props.updates.map(item => {
      let website = {};
      loading[item.id] = false;
      if (item.app_id !== -1 && this.props.dashboard.apps[item.app_id].sso_group_id) {
        website = this.props.sso_list[0];
        website.logo = '/resources/other/google-logo.png';
        website.information = {
          login: {name: 'login', placeholder: "Login", priority: 0, type: "text"},
          password: {name: 'password', placeholder: "Password", priority: 1, type: "password"}
        };
        website.app_name = website.name;
      }
      else {
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
            stateWebsites[item.id] = website;
            this.setState({websites: stateWebsites});
          });
        }
      }
      stateWebsites[item.id] = website;
    });
    this.setState({websites: stateWebsites, loadingDelete: loading});
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
    const sso_group = app.sso_group_id ? this.props.dashboard.sso_groups[app.sso_group_id] : -1;
    if ((item.app_id === -1 && item.team_card_id === -1) || (item.team_card_id !== -1 && (card.type !== "teamEnterpriseCard"
      && card.team_user_filler_id !== meId && card.team_user_filler_id !== -1))) {
      this.state.type[item.id] = 'new';
      return <span>New Account</span>;
    }
    else if (item.app_id !== -1 &&
      ((!app.sso_group_id && Object.keys(app.account_information).length > 0 && app.account_information.login !== '')
        || (!app.sso_group_id && Object.keys(card.account_information).length > 0 && card.account_information.login !== '')
      || (app.sso_group_id &&
          Object.keys(sso_group.account_information).length > 0 && sso_group.account_information.login !== ''))) {
      this.state.type[item.id] = 'password';
      return <span>Password update</span>;
    }
    else if (item.app_id !== -1 &&
      ((!app.sso_group_id && Object.keys(app.account_information).length === 0 || app.account_information.login === '')
        || (!app.sso_group_id && Object.keys(card.account_information).length > 0 || card.account_information.login === '')
        || (app.sso_group_id && Object.keys(sso_group.account_information).length > 0
          && sso_group.account_information.login !== ''))
      && (item.team_card_id !== -1 && (card.type === "teamEnterpriseCard"
        || card.team_user_filler_id === meId || card.team_user_filler_id === -1))) {
      this.state.type[item.id] = 'account';
      return <span>Account update</span>;
    }
  };
  deleteUpdate = (id) => {
    let loading = {...this.state.loadingDelete};
    loading[id] = true;
    this.setState({loadingDelete: loading});
    this.setState({loading: true});
    this.props.dispatch(deleteUpdate({id: id})
    ).then(() => {
      loading[id] = false;
      this.setState({loadingDelete: loading});
    })
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
          {this.props.updates.map(item => {
            const meId = item.team_id !== -1 ? this.props.teams[item.team_id].my_team_user_id : -1;
            const card = item.team_card_id !== -1 ? this.props.team_apps[item.team_card_id] : -1;
            const app = item.app_id !== -1 ? this.props.dashboard.apps[item.app_id] : -1;
            const website = this.state.websites[item.id];
            return (
              <Grid.Column key={item.id} className="showSegment update">
                <Loader size='small' active={this.state.loadingDelete[item.id]} inline='centered'/>
                {!this.state.loadingDelete[item.id] &&
                  <React.Fragment>
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
                    <Icon name="trash" onClick={() => this.deleteUpdate(item.id)}/>
                    <a onClick={() => this.openModal({
                      item: item,
                      website: website,
                      account_information: item.account_information})}>
                      Manage now <Icon name="caret right"/>
                    </a>
                  </React.Fragment>}
              </Grid.Column>)
          })}
        </Grid>
      </Container>
    )
  }
}

export default UpdatesContainer;