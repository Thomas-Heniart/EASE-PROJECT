import React, {Component} from 'react';
import {connect} from 'react-redux';
import {logoLetter} from "../../../utils/utils";
import {Icon, Dropdown, Grid, Popup, Table, Checkbox, Button} from 'semantic-ui-react';
import {appAdded, createProfile, editAppCredentials} from "../../../actions/dashboardActions";
import {
  catalogImportationAddPersonalApp, deleteImportedAccount, testCredentials
} from "../../../actions/catalogActions";
import {
  catalogImportationCreateCard, teamEditEnterpriseCardReceiver, teamEditSingleCardCredentials
} from "../../../actions/appsActions";

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
      loading: false,
      loadingBack: false,
      profileChose: -1,
      accountSetting: {},
      accountLocation: {},
      checkImport: {},
      checkPro: {},
      emptyApps: {},
      seePassword: {},
      accountsNumber: 0,
      dropdownOpened: false
    }
  }
  componentWillMount(){
    this.searchEmptyApps();
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
  changeUpdate = (id, {value}) => {
    this.state.emptyApps[id].update = value;
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


  chooseAccountSetting = (id, {value}) => {
    this.state.accountSetting[id] = value;
  };
  chooseLocation = (id, props) => {
    this.state.accountLocation[id] = {
      room_id: props.options[props.value].room_id,
      team_id: props.options[props.value].team_id
    };
  };


  searchEmptyApps = () => {
    this.setState({loadingBack: true});
    const emptyApps = {};
    const checkImport = {};
    const checkPro = {};
    Object.keys(this.props.importedAccounts).map(account_id => {
      const account = this.props.importedAccounts[account_id];
      checkImport[account_id] = true;
      checkPro[account_id] = true;
      Object.keys(this.props.apps).filter(app_id => {
        let app = this.props.apps[app_id];
        let card = app.team_id ? this.props.team_apps[app.team_card_id] : null;
        if (app.website && ((app.team_id &&
          (card.type === 'teamEnterpriseCard' && card.receivers.find(receiver => (receiver.team_user_id === this.props.teams[app.team_id].my_team_user_id)).empty)
          || (card.type === 'teamSingleCard' && card.team_user_filler_id === this.props.teams[app.team_id].my_team_user_id))
          || (!app.team_id && app.empty)) && (app.website.id === account.website_id || app.website.login_url === account.url)) {
          emptyApps[app.id] = {
            app: app,
            team_card: card,
            update: emptyApps[app.id] ? emptyApps[app.id].update : account_id,
            accounts: emptyApps[app.id] ? {...emptyApps[app_id].accounts} : {}
          };
          emptyApps[app.id].accounts[account.id] = account;
        }
      });
    });
    console.log('[EMPTY_APPS]: ', emptyApps);
    this.setState({
      loadingBack: false,
      emptyApps: emptyApps,
      checkImport: checkImport,
      checkPro: checkPro,
      view: Object.keys(emptyApps).length === 0 ? 2 : 1
    });
  };


  beforeSendImport = () => {
    this.setState({loading: true});
    if (this.state.profileChose === -1 && Object.keys(this.state.checkPro).find(id => (this.state.checkPro[id] === false))) {
      const profileChoose = Object.keys(this.props.profiles).find(profile_id => (
        this.props.profiles[profile_id].team_id === -1
      ));
      if (!profileChoose) {
        this.props.dispatch(createProfile({
          name: 'Me',
          column_index: 1
        })).then(response => {
          this.state.profileChose = response.id;
          this.sendImport();
        });
      }
      else {
        this.state.profileChose = profileChoose;
        this.sendImport();
      }
    }
    else
      this.sendImport();
  };


  sendImport = () => {
    let calls = [];
    Object.keys(this.props.importedAccounts).map(account_id => {
      const account = this.props.importedAccounts[account_id];
      const setting = this.state.accountSetting[account_id] || this.state.accountSetting[account_id] === 0 ? this.state.accountSetting[account_id] : null;
      const room_id = this.state.accountLocation[account_id] ? this.state.accountLocation[account_id].room_id : null;
      const team_id = this.state.accountLocation[account_id] ? this.state.accountLocation[account_id].team_id : null;
      if (this.state.checkImport[account_id] && this.state.checkPro[account_id] && (setting || setting === 0) && room_id) {
        let receivers = {};
        this.props.teams[team_id].rooms[room_id].team_user_ids.map(item => {
          if (setting === 0)
            receivers[item] = {allowed_to_see_password: true};
          else {
            if (this.props.teams[team_id].my_team_user_id === item)
              receivers[item] = {account_information: {login: account.login, password: account.password}};
            else
              receivers[item] = {account_information: {login: "", password: ""}};
          }
        });
        calls.push(this.props.dispatch(catalogImportationCreateCard({
          account: account,
          team_id: team_id,
          room_id: room_id,
          receivers: receivers,
          single: setting === 0
        })));
        this.finish(account_id);
      }
      else if (this.state.checkImport[account_id] && !this.state.checkPro[account_id]) {
        calls.push(this.props.dispatch(catalogImportationAddPersonalApp({
          account: account,
          selectedProfile: this.state.profileChose
        })));
        this.finish(account_id);
      }
      else if (!this.state.checkImport[account_id])
        this.finish(account_id);
    });
  };


  updateApp = () => {
    this.setState({loading: true});
    Object.keys(this.state.emptyApps).map(app_id => {
      const app = this.state.emptyApps[app_id];
      const accounts = this.state.emptyApps[app_id].accounts;
      const account = app.update !== 0 ? accounts[Number(app.update)] : null;
      if (account) {
        let acc_info = {};
        acc_info.login = account.login;
        acc_info.password = account.password;
        if (app.team_card !== null) {
          if (app.app.type === 'teamEnterpriseApp') {
            this.props.dispatch(teamEditEnterpriseCardReceiver({
              team_id: app.app.team_id,
              team_card_id: app.app.team_card_id,
              team_card_receiver_id: this.props.team_apps[app.app.team_card_id].receivers.find(receiver => (
                this.props.teams[app.app.team_id].my_team_user_id === receiver.team_user_id
              )).id,
              account_information: acc_info
            })).then(response => {
              delete this.state.emptyApps[app_id];
              this.finish(account.id);
            });
          }
          else {
            this.props.dispatch(teamEditSingleCardCredentials({
              team_card: app.team_card,
              account_information: acc_info
            })).then(response => {
              delete this.state.emptyApps[app_id];
              this.finish(account.id);
            });
          }
        }
        else {
          this.props.dispatch(editAppCredentials({
            app: account.app,
            account_information: acc_info
          })).then(() => {
            delete this.state.emptyApps[app_id];
            this.finish(account.id);
          });
        }
        this.props.dispatch(appAdded({
          app: app.app
        }));
      }
      else {
        delete this.state.emptyApps[app_id];
        if (this.state.view === 1 && Object.keys(this.state.emptyApps).length === 0)
          this.setState({view: 2});
        this.setState({loading: false});
      }
    });
  };


  finish = (account_id) => {
    if (this.props.importedAccounts[account_id]) {
      this.props.dispatch(deleteImportedAccount({
        id: account_id
      })).then(res => {
        if (this.state.view === 1 && Object.keys(this.state.emptyApps).length === 0)
          this.setState({view: 2});
        delete this.props.importedAccounts[account_id];
        this.setState({emptyApps: this.state.emptyApps, loading: false});
      });
    }
    else {
      if (this.state.view === 1 && Object.keys(this.state.emptyApps).length === 0)
        this.setState({view: 2});
      delete this.props.importedAccounts[account_id];
      this.setState({emptyApps: this.state.emptyApps, loading: false});
    }
  };


  render() {
    const {
      error,
      loadingLogo,
      loadingSending,
      importAccounts,
      importedAccounts
    } = this.props;
    const accountSettingOptions = [
      {text: 'Shared account with my team', value: 0},
      {text: 'Professional nominative account', value: 1}
    ];
    const sendInOptions = [];
    let emptyApps = null;
    let accounts = null;


    if (this.state.view === 1)
      emptyApps = Object.keys(this.state.emptyApps).map(app_id => {
        const accounts = this.state.emptyApps[app_id].accounts;
        const app = this.state.emptyApps[app_id].app;
        const team_card = this.state.emptyApps[app_id].team_card;
        const options = [
          {text: 'It’s another account - I’ll setup later', value: 0}
        ];
        Object.keys(accounts).map(account_id => {
          const account = accounts[account_id];
          const password =
            <span>{account.login} | {this.state.seePassword[account.id] === 'text' ? account.password : '• • • • • • • •'}
              <Icon onClick={e => this.seePasswordOptions(e, account.id)}
                    name={this.state.seePassword[account.id] === 'text' ? 'eye' : 'hide'}/></span>;
          options.push({text: password, value: account.id});
        });
        return <Table.Row key={app_id}>
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
            <Dropdown selection
                      options={options}
                      className='table_update'
                      defaultValue={options[1].value}
                      onChange={(e, {value}) => this.changeUpdate(app_id, {value})}/>
          </Table.Cell>
        </Table.Row>
      });


    if (this.state.view === 2) {
      let i = 0;
      Object.keys(this.props.teams).map(team_id => {
        const team = this.props.teams[team_id];
        {Object.keys(team.rooms).map(room_id => {
          const room = team.rooms[room_id];
          sendInOptions.push({text: `#${room.name} (${team.name})`, value: i++, team_id: team.id, room_id: room.id})});
        }
      });
      accounts = Object.keys(importedAccounts).map(account_id => {
        const account = importedAccounts[account_id];
        return <Table.Row key={account_id}>
          <Table.Cell className='center'>
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
              <span className='span_table'>{account.name}</span>
              <span className='span_table'>{account.login}</span>
              <span className='span_table'>{this.state.seePassword[account_id] === 'text' ? account.password : '• • • • • • • •'} <Icon
                onClick={e => this.seePasswordOptions(e, account_id)}
                name={this.state.seePassword[account_id] === 'text' ? 'eye' : 'hide'}/></span>
            </div>
          </Table.Cell>
          <Table.Cell className='center'>
            {this.state.checkImport[account_id] &&
            <Checkbox toggle name='checkPro' checked={this.state.checkPro[account_id]}
                      onChange={(e, {name, checked}) => this.check(account_id, name, checked)}/>}
          </Table.Cell>
          {(this.state.checkImport[account_id] && this.state.checkPro[account_id]) &&
          <React.Fragment>
            <Table.Cell>
              <Dropdown className='table_import' selection options={accountSettingOptions} placeholder='Account setting'
                        onChange={(e, {value}) => this.chooseAccountSetting(account_id, {value})}/>
            </Table.Cell>
            <Table.Cell>
              <Dropdown className='table_import' selection options={sendInOptions} placeholder='Send in...'
                        onChange={(e, {...props}) => this.chooseLocation(account_id, props)}/>
            </Table.Cell>
          </React.Fragment>}
          {!this.state.checkImport[account_id] &&
          <React.Fragment>
            <Table.Cell className='cell_relative'>
              <span className='span_table'>This account will be deleted once importation is completed</span>
            </Table.Cell>
            <Table.Cell className='cell_relative' />
          </React.Fragment>}
          {(this.state.checkImport[account_id] && !this.state.checkPro[account_id]) &&
          <React.Fragment>
            <Table.Cell className='cell_relative'>
              <span className='span_table'>This account is not related to your company or your team in any way</span>
            </Table.Cell>
            <Table.Cell className='cell_relative'/>
          </React.Fragment>}
        </Table.Row>
      });
    }


    return (
      <React.Fragment>
        {this.state.view === 1 &&
        <p style={{marginBottom: '20px', marginLeft: '70px', marginTop: '10px', color:'#373b60', fontWeight:'bold'}}>
          1. Match imported accounts with empty apps in your team ({Object.keys(this.state.emptyApps).length})
        </p>}
        {this.state.view === 2 &&
        <p style={{marginBottom: '20px', marginLeft: '70px', marginTop: '10px', color:'#373b60', fontWeight:'bold'}}>
          2. Manage other accounts ({Object.keys(importedAccounts).length})
        </p>}
        <Grid id='accounts'>
          <Grid.Column width={14}>


            {this.state.view === 1 &&
            <Table singleLine>
              <Table.Header>
                <Table.Row>
                  <Table.HeaderCell>YOUR EMPTY APPS</Table.HeaderCell>
                  <Table.HeaderCell/>
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
                  <Table.HeaderCell className='center'>
                    IMPORT
                    <div><Checkbox toggle name='checkImport' defaultChecked onChange={this.checkAll}/></div>
                  </Table.HeaderCell>
                  <Table.HeaderCell>ACCOUNTS</Table.HeaderCell>
                  <Table.HeaderCell className='center'>
                    ACCOUNT
                    <div>Perso <Checkbox toggle name='checkPro' defaultChecked onChange={this.checkAll}/> Pro</div>
                  </Table.HeaderCell>
                  <Table.HeaderCell>ACCOUNT SETTING <Popup
                    size='mini'
                    inverted
                    trigger={<Icon name='help circle'/>}
                    content={
                      <span><strong><u>Shared account</u></strong>: the login and password of the account are shared between team members.<br/>
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
          <Grid.Column width={2}/>
        </Grid>
        <div className='importation_bottom'>
          {this.state.view === 2 &&
          <Button
            className='button_left'
            onClick={this.searchEmptyApps}
            loading={this.state.loadingBack}
            disabled={this.state.loading || this.state.loadingBack}
            content={<span><Icon name='arrow left'/> Back</span>} />}
          <span className='span_step'>{this.state.view === 1 ? 'Importation step 1 out of 2' : 'Importation step 2 out of 2'}</span>
          <Button
            positive
            className='button_right'
            loading={this.state.loading}
            disabled={this.state.loading}
            onClick={this.state.view === 1 ? this.updateApp : this.beforeSendImport}
            content={this.state.view === 1 ? <span>Next <Icon name='arrow right'/></span> : 'Done'} />
        </div>
      </React.Fragment>
    )
  }
}

export default DisplayAccounts;