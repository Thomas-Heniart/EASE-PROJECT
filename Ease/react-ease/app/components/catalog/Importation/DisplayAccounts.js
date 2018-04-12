import React, {Component} from 'react';
import {connect} from 'react-redux';
import {logoLetter} from "../../../utils/utils";
import {testCredentials} from "../../../actions/catalogActions";
import {Icon, Dropdown, Grid, Popup, Table, Checkbox} from 'semantic-ui-react';
import {teamEditEnterpriseCardReceiver, teamEditSingleCardCredentials} from "../../../actions/appsActions";
import {appAdded, editAppCredentials, updateAccepted} from "../../../actions/dashboardActions";

@connect(store => ({
  teams: store.teams,
  apps: store.dashboard.apps,
  team_apps: store.team_apps,
  profiles: store.dashboard.profiles
}))
class DisplayAccounts extends Component {
  constructor(props) {
    super(props);
    this.state = {
      view: 2,
      checkImport: {},
      checkPro: {},
      emptyApps: {},
      seePassword: {},
      accountsNumber: 0,
      dropdownOpened: false
    }
  }
  componentWillMount(){
    const emptyApps = {};
    const checkImport = {};
    const checkPro = {};
    Object.keys(this.props.importedAccounts).map(account_id => {
      const account = this.props.importedAccounts[account_id];
      const emptyApp = this.props.apps[Object.keys(this.props.apps).find(app_id => {
        const app = this.props.apps[app_id];
        return app.website && app.empty && (app.website.id === account.website_id || app.website.login_url === account.url)
      })];
      checkImport[account_id] = true;
      checkPro[account_id] = true;
      console.log('[EMPTY_APPS]: ', emptyApp);
      if (emptyApp) {
        emptyApps[account_id] = account;
        emptyApps[account_id].update = true;
        emptyApps[account_id].app = emptyApp;
        emptyApps[account_id].team_card = emptyApp.team_id ? this.props.team_apps[emptyApp.team_card_id] : null;
      }
    });
    this.setState({emptyApps: emptyApps, checkImport: checkImport, checkPro: checkPro}, this.props.getLogo());
  }
  componentDidMount(){
    const seePassword = {};
    Object.keys(this.props.importedAccounts).map(item => {
      seePassword[item] = 'password';
    });
    this.setState({accountsNumber: Object.keys(this.props.importedAccounts).length, seePassword: seePassword});
  }
  checkAll = (e, {name, checked}) => {
    const check = {};
    Object.keys(this.state[name]).map(account_id => {
      check[account_id] = checked;
    });
    this.setState({[name]: check});
  };
  check = (id, name, checked) => {
    const check = {...this.state[name]};
    check[id] = checked;
    // this.state[name][id] = checked;
    this.setState({[name]: check});
  };
  changeUpdate = (id) => {
    this.state.emptyApps[id].update = !this.state.emptyApps[id].update;
  };
  seePasswordOptions = (e, id) => {
    e.stopPropagation();
    const seePassword = {...this.state.seePassword};
    if (seePassword[id] === 'password')
      seePassword[id] = 'text';
    else
      seePassword[id] = 'password';
    this.setState({seePassword: seePassword});
  };
  testConnection = (item) => {
    this.props.dispatch(testCredentials({
      account_information: {login: item.login, password: item.password},
      website_id: item.website_id
    }));
  };


  updateApp = () => {
    this.setState({loading: true});
    Object.keys(this.state.emptyApps).map(account_id => {
      const account = this.state.emptyApps[account_id];
      if (account.update) {
        let acc_info = {};
        acc_info.login = account.login;
        acc_info.password = account.password;
        if (account.team_card !== null) {
          if (account.app.type === 'teamEnterpriseApp') {
            this.props.dispatch(teamEditEnterpriseCardReceiver({
              team_id: account.app.team_id,
              team_card_id: account.app.team_card_id,
              team_card_receiver_id: this.props.team_apps[account.app.team_card_id].receivers.find(receiver => (
                this.props.teams[account.app.team_id].my_team_user_id === receiver.team_user_id
              )).id,
              account_information: acc_info
            })).then(response => {
              this.finish();
            });
          }
          else {
            this.props.dispatch(teamEditSingleCardCredentials({
              team_card: account.team_card,
              account_information: acc_info
            })).then(response => {
              this.finish();
            });
          }
        }
        else {
          this.props.dispatch(editAppCredentials({
            app: account.app,
            account_information: acc_info
          })).then(() => {
            this.finish();
          });
        }
        this.props.dispatch(appAdded({
          app: account.app
        }));
      }
    });
  };


  render() {
    const {
      error,
      toPending,
      selectRoom,
      loadingLogo,
      selectedRoom,
      cancelPending,
      selectProfile,
      loadingSending,
      importAccounts,
      accountsPending,
      selectedProfile,
      importedAccounts,
    } = this.props;
    const accountSettingOptions = [
      {text: 'Shared account with my team', value: 0},
      {text: 'Professional nominative account', value: 1}
    ];
    const sendInOptions = [];
    let emptyApps = null;
    let accounts = null;


    if (this.state.view === 1)
      emptyApps = Object.keys(this.state.emptyApps).map(account_id => {
      const account = this.state.emptyApps[account_id];
      const app = account.app;
      const team_card = account.team_card;
      const password = <span>{account.login} | {this.state.seePassword[account_id] === 'text' ? account.password : '• • • • • • • •'} <Icon onClick={e => this.seePasswordOptions(e, account_id)} name={this.state.seePassword[account_id] === 'text' ? 'eye' : 'hide'}/></span>;
      const options = [
        {text: password, value: 1},
        {text: 'It’s another account - I’ll setup later', value: 0}
      ];
      return <Table.Row key={account_id}>
        <Table.Cell className='app_indication'>
          {(app.website.logo && app.website.logo.length > 0) && <img src={app.website.logo}/>}
          {(!app.website.logo || app.website.logo.length < 1) &&
          <div className='logo_letter'>
            <p>{logoLetter(team_card ? team_card.name : app.name)}</p>
          </div>}
          <div className='name'>
            <span>{team_card ? team_card.name : app.name}</span>
            {team_card &&
            <span>#{this.props.teams[app.team_id].rooms[team_card.channel_id].name}</span>}
            {!team_card &&
            <span>#{this.props.profiles[app.profile_id].name}</span>}
          </div>
        </Table.Cell>
        <Table.Cell>
          {(team_card && team_card.type === "teamEnterpriseCard") &&
          <span><strong><u>My nominative</u></strong> account: </span>}
          {(team_card && team_card.type === "teamSingleCard") &&
          <span><strong><u>Shared</u></strong> account: </span>}
          {!team_card &&
          <span><strong><u>Personal</u></strong> account: </span>}
        </Table.Cell>
        <Table.Cell>
          <Dropdown options={options} selection defaultValue={1} onChange={e => this.changeUpdate(account_id)}/>
        </Table.Cell>
      </Table.Row>
    });

    if (this.state.view === 2) {
      Object.keys(this.props.teams).map(team_id => {
        const team = this.props.teams[team_id];
        {Object.keys(team.rooms).map(room_id => {
          const room = team.rooms[room_id];
          sendInOptions.push({text: `#${room.name} (${team.name})`, value: room.id})})
        }
      });
      accounts = Object.keys(importedAccounts).map(account_id => {
        const account = importedAccounts[account_id];
        return <Table.Row key={account_id}>
          <Table.Cell>
            <Checkbox toggle name='checkImport' checked={this.state.checkImport[account_id]}
                      onChange={(e, {name, checked}) => this.check(account_id, name, checked)}/>
          </Table.Cell>
          <Table.Cell className='app_indication'>
            {(account.logo && account.logo.length > 0) && <img src={account.logo}/>}
            {(!account.logo || account.logo.length < 1) &&
            <div className='logo_letter'>
              <p>{logoLetter(account.name)}</p>
            </div>}
            <div className='name'>
              <span>{account.name}</span>
              <span>{account.login}</span>
              <span>{this.state.seePassword[account_id] === 'text' ? account.password : '• • • • • • • •'} <Icon
                onClick={e => this.seePasswordOptions(e, account_id)}
                name={this.state.seePassword[account_id] === 'text' ? 'eye' : 'hide'}/></span>
            </div>
          </Table.Cell>
          <Table.Cell>
            {this.state.checkImport[account_id] &&
            <Checkbox toggle name='checkPro' checked={this.state.checkPro[account_id]}
                      onChange={(e, {name, checked}) => this.check(account_id, name, checked)}/>}
          </Table.Cell>
          {(this.state.checkImport[account_id] && this.state.checkPro[account_id]) &&
          <React.Fragment>
            <Table.Cell>
              <Dropdown selection options={accountSettingOptions}/>
            </Table.Cell>
            <Table.Cell>
              <Dropdown selection options={sendInOptions}/>
            </Table.Cell>
          </React.Fragment>}
          {!this.state.checkImport[account_id] &&
          <Table.Cell>
            <span>This account will be deleted once importation is completed</span>
          </Table.Cell>}
          {(this.state.checkImport[account_id] && !this.state.checkPro[account_id]) &&
          <Table.Cell>
            <span>This account is not related to your company or your team in any way</span>
          </Table.Cell>}
        </Table.Row>
      });
    }


    return (
      <React.Fragment>
        <p style={{marginBottom: '20px', marginLeft: '70px', marginTop: '10px'}}>
          <span style={{color:'#45c997',fontWeight:'bold'}}>{this.state.accountsNumber} accounts detected! </span>
          Make sure your data are put in the right columns, then import.
        </p>
        <Grid id='accounts'>
          <Grid.Column width={11}>

            {this.state.view === 1 &&
            <Table singleLine>
              <Table.Header>
                <Table.Row>
                  <Table.HeaderCell>YOUR EMPTY APPS</Table.HeaderCell>
                  <Table.HeaderCell />
                  <Table.HeaderCell>IMPORTED ACCOUNTS CORRESPONDING</Table.HeaderCell>
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {emptyApps}
              </Table.Body>
            </Table>}

            {this.state.view === 2 &&
            <Table singleLine>
              <Table.Header>
                <Table.Row>
                  <Table.HeaderCell>
                    IMPORT
                    <div><Checkbox toggle name='checkImport' defaultChecked onChange={this.checkAll}/></div>
                  </Table.HeaderCell>
                  <Table.HeaderCell>ACCOUNTS</Table.HeaderCell>
                  <Table.HeaderCell>
                    ACCOUNT
                    <div>Perso <Checkbox toggle name='checkPro' defaultChecked onChange={this.checkAll}/> Pro</div>
                  </Table.HeaderCell>
                  <Table.HeaderCell>ACCOUNT SETTING <Popup
                    inverted
                    trigger={<Icon name='help circle'/>}
                    content={
                      <span><strong><u>Shared account</u></strong>: the login and password of the account are shared between team members.
                      <strong><u>Professional nominative account</u></strong>: Each user has his own login and password for this tool.</span>
                    }/>
                  </Table.HeaderCell>
                  <Table.HeaderCell>SEND IN...</Table.HeaderCell>
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {accounts}
              </Table.Body>
            </Table>}


          </Grid.Column>
          <Grid.Column width={5}/>
        </Grid>
      </React.Fragment>
    )
  }
}

export default DisplayAccounts;