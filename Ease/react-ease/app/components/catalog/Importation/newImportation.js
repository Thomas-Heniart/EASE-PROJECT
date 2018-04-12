import React from 'react';
import {connect} from "react-redux";
import PasteStep from "./PasteStep";
import Explication from "./Explication";
import DisplayAccounts from "./DisplayAccounts";
import ChromeFirstStep from "./ChromeFirstStep";
import ChromeSecondStep from "./ChromeSecondStep";
import ChoosePasswordManager from "./ChoosePasswordManager";
import * as api from "../../../utils/api";
import {getLogo} from "../../../utils/api";
import {Loader, Message} from 'semantic-ui-react';
import {appAdded, createProfile} from "../../../actions/dashboardActions";
import {handleSemanticInput, isEmail, reflect} from "../../../utils/utils";
import {importAccount, deleteImportedAccount} from "../../../actions/catalogActions";
import {createTeamChannel, addTeamUserToChannel} from "../../../actions/channelActions";
import {teamCreateSingleApp, teamCreateAnySingleCard, teamCreateLinkCard} from "../../../actions/appsActions";
import {
  catalogAddAnyApp, catalogAddBookmark, catalogAddClassicApp, getImportedAccounts
} from "../../../actions/catalogActions";

function json(fields, separator, csv, dispatch) {
  const array = csv.split('\n');
  let calls = [];
  for (let i = 0; i < array.length; i++) {
    let separatorCounter = 0;
    const object = Object.keys(fields);
    let url = false;
    object.map(item => {
      if (fields[item] === 'url')
        url = true;
    });
    for (let j = 0; j < array[i].length; j++) {
      if (array[i][j] === separator)
        separatorCounter++;
    }
    if (separatorCounter >= 3 && url === true) {
      let item = {};
      const field = array[i].split(separator);
      let l = 0;
      for (let k = 0; k < field.length; k++) {
        if (fields[object[k]] === 'login' && field[k + 1] && isEmail(field[k + 1].replace(/^["]+|["]+$/g, '')))
          k++;
        item[fields[object[l++]]] = field[k].replace(/^["]+|["]+$/g, '');
      }
      if (!item.url.trim().startsWith('http://') && !item.url.trim().startsWith('https://') && item.url.trim() !== '')
        item.url = "https://" + item.url.trim();
      if (item.url.trim() !== '' && item.url.trim().match(/^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})(\/?)/) !== null) {
        calls.push(dispatch(importAccount({
          name: item.name.trim() ? item.name.trim() : '',
          url: item.url.trim(),
          account_information: {
            login: {name: "login", value: item.login.trim() ? item.login.trim() : ''},
            password: {name: "password", value: item.password.trim() ? item.password.trim() : ''}
          }
        })))
      }
    }
  }
  if (calls.length)
    return calls;
  else if (separator === ',')
    return json(fields, '\t', csv, dispatch);
  else
    return null;
}

@connect(store => ({
  fetching: store.catalog.fetching,
  websites: store.catalog.websites,
  dashboard: store.dashboard,
  profiles: store.dashboard.profiles,
  teams: store.teams,
  user: store.common.user
}))
class Importations extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      view: 1,
      loadingLogo: {},
      passwordManager: 0,
      chromeLogin: '',
      chromePassword: '',
      separator: ',',
      paste: '',
      error: '',
      specialError: false,
      location: '',
      loading: false,
      loadingDelete: false,
      loadingSending: false,
      fields: {},
      importedAccounts: {},
      accountsPending: {},
      errorAccounts: {},
      profiles: {},
      selectedProfile: -1,
      teamsInState: {},
      roomName: {},
      roomAdded: false,
      selectedTeam: -1,
      selectedRoom: -1,
      fieldProblem: {name: '', id: -1}
    };
  }
  componentWillMount() {
    this.setState({loading: true});
    const profiles = {...this.props.profiles};
    let newTeams = {};
    Object.keys(this.props.teams).map(item => {
      const team  = this.props.teams[item];
      newTeams[item] = {...team};
      newTeams[item].rooms = {};
      Object.keys(team.rooms).map(room_id =>  {
        newTeams[item].rooms[room_id] = {...team.rooms[room_id]}
      });
    });
    const keys = Object.keys(newTeams);
    const roomName = {};
    const roomAdded = {};
    keys.map(item => {
      roomName[item] = "";
      roomAdded[item] = false;
      return item;
    });
    this.props.dispatch(getImportedAccounts()).then(response => {
      if (response.length) {
        const accounts = {};
        response.map(item => {
          accounts[item.id] = {
            id: item.id,
            url: item.url,
            name: item.name,
            website_id: item.website_id,
            login: item.account_information.login.value,
            password: item.account_information.password.value
          }
        });
        this.setState({
          importedAccounts: accounts,
          view: 4,
          fields: {field1: 'url', field2: 'name', field3: 'login', field4: 'password'}
        });
      }
      this.setState({loading:false, profiles:profiles, teamsInState:newTeams, roomName:roomName, roomAdded:roomAdded});
    }).catch(err => {
      this.setState({error: err, loading: false});
    });
  }
  getLogo = () => {
    let loading = {};
    const importedAccounts = {...this.state.importedAccounts};
    Object.keys(importedAccounts).map(item => {
      loading[item] = true;
      this.setState({loadingLogo: loading});
      if (this.state.importedAccounts[item].website_id === -1) {
        getLogo({url: this.state.importedAccounts[item].url}).then(response => {
          if (response !== '/resources/icons/link_app.png')
            importedAccounts[item].logo = response;
          else if (this.state.importedAccounts[item].login
            && this.state.importedAccounts[item].password && response === '/resources/icons/link_app.png')
            importedAccounts[item].logo = '';
          else
            importedAccounts[item].logo = response;
          loading[item] = false;
          this.setState({loadingLogo: loading});
        });
      }
      else {
        importedAccounts[item].logo = this.props.websites.find(website => (importedAccounts[item].website_id === website.id)).logo;
        loading[item] = false;
        this.setState({loadingLogo: loading});
      }
    });
    this.setState({importedAccounts: importedAccounts});
  };
  handleInput = handleSemanticInput.bind(this);
  handleAppInfo = (e, {name, value, idapp}) => {
    const importedAccounts = {...this.state.importedAccounts};
    importedAccounts[idapp].name = value;
    this.setState({importedAccounts: importedAccounts});
  };
  selectAccount = (account) => {
    api.dashboard.getAppPassword({
      app_id: account.sso_app_ids[0]
    }).then(response => {
      this.setState({
        chromeLogin: account.account_information.login,
        chromePassword: response.password
      });
    });
  };
  resetChromeCredentials = () => {
    this.setState({chromeLogin: '', chromePassword: ''});
  };
  toPending = (id) => {
    if (this.state.selectedProfile === -1 && this.state.selectedRoom === -1) {
      this.setState({error:"Choose where you want to put your apps!"})
    }
    else {
      const accountsPending = {...this.state.accountsPending};
      const importedAccounts = {...this.state.importedAccounts};
      if (importedAccounts[id].url.match(/^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})(\/?)/) !== null && importedAccounts[id].name !== '') {
        accountsPending[id] = {...this.state.importedAccounts[id]};
        delete importedAccounts[id];
      }
      else if (importedAccounts[id].url.match(/^(https?:\/\/)?([\da-z.-]+)\.([a-z.]{2,6})(\/?)/) !== null && importedAccounts[id].name === '')
        this.setState({error: 'Please name the account before importing it.', fieldProblem: {name: 'name', id: id}});
      else
        this.setState({error: 'It is not an URL', fieldProblem: {name: 'url', id: id}});
      this.setState({importedAccounts: importedAccounts, accountsPending: accountsPending});
    }
  };
  cancelPending = (id) => {
    const accountsPending = {...this.state.accountsPending};
    const importedAccounts = {...this.state.importedAccounts};
    importedAccounts[id] = accountsPending[id];
    delete accountsPending[id];
    this.setState({importedAccounts: importedAccounts, accountsPending: accountsPending});
  };
  selectProfile = () => {
    let profileChoose = null;
    Object.keys(this.props.profiles).map(item => {
      if (profileChoose === null && this.props.profiles[item].team_id === -1)
        profileChoose = this.props.profiles[item].id;
    });
    this.setState({
      selectedProfile: profileChoose !== null ? profileChoose : 0,
      selectedTeam: -1,
      selectedRoom: -1,
      location: 'Personal Apps',
      error: ""
    });
  };
  selectRoom = (teamId, roomId, name) => {
    this.setState({ selectedTeam: teamId, selectedRoom: roomId, selectedProfile: -1, location: `#${name}`, error:""});
  };
  changeOrderField = (e, {name, value}) => {
    let fields = this.state.fields;
    if (value !== '-') {
      Object.keys(fields).map(item => {
        if (fields[item] === value)
          fields[item] = fields[name];
      });
    }
    Object.keys(fields).map(item => {
      if (item === name)
        fields[item] = value;
    });
    this.setState({fields: fields});
  };
  eventListener = event => {
    if (event.detail.success === true && event.detail.msg.length > 0) {
      let calls = [];
      event.detail.msg.map((item, idx) => {
        calls.push(this.props.dispatch(importAccount({
          id: idx,
          name: '',
          url:  item.website.startsWith("http") === false && item.website !== '' ? "https://" + item.website : item.website,
          website_id: -1,
          account_information: {
            login: {name:"login", value: item.login},
            password: {name:"password", value: item.pass}
          }
        })));
      });
      Promise.all(calls.map(reflect)).then(response => {
        const json = response.filter(item => {
          if (item.error === false)
            return item;
        }).map(item => {
          return item.data;
        });
        if (json.length) {
          const acc = {};
          json.map(item => {
            acc[item.id] = {
              id: item.id,
              url: item.url,
              name: item.name,
              login: item.account_information.login.value,
              password: item.account_information.password.value,
              website_id: item.website_id,
            }
          });
          this.setState({
            importedAccounts: acc,
            view: 4,
            error: '',
            fields: {field1: 'url', field2: 'name', field3: 'login', field4: 'password'}
          });
        }
        else
          this.setState({view: 2, error: 'Darn, that didn’t work! Chrome is being delicate... Please try one more time or contact our customer support.'});
      }).catch(err => {
        this.setState({view: 2, error: 'Darn, that didn’t work! Chrome is being delicate... Please try one more time or contact our customer support.'});
      });
    }
    else if (event.detail.msg === [] || event.detail.msg.length === 0)
      this.setState({view: 2, specialError: true});
    else
      this.setState({view: 2, error: event.detail.msg});
  };
  changeView = () => {
    this.setState({loading: true});
    if (this.state.view === 1 && this.state.passwordManager === 1)
      this.setState({view: 3, loading: false, error: '', specialError: false});
    else if (this.state.view === 2 && this.state.passwordManager === 2) {
      this.setState({view: 3, loading: false, error: '', specialError: false});
      document.dispatchEvent(new CustomEvent("ScrapChrome", {detail:{login:this.state.chromeLogin,password:this.state.chromePassword}}));
      document.addEventListener("ScrapChromeResult", (event) => this.eventListener(event));
    }
    else if (this.state.view === 3 && this.state.paste !== '') {
      const calls = json(this.state.fields, this.state.separator, this.state.paste, this.props.dispatch);
      if (calls === null)
        this.setState({error: 'Darn, that didn’t work! Make sure the text pasted contains logins and passwords properly separated.', loading: false});
      else {
        Promise.all(calls.map(reflect)).then(response => {
          const json = response.filter(item => {
            if (item.error === false)
              return item;
          }).map(item => {
            return item.data;
          });
          if (json.length) {
            const accounts = {};
            json.map(item => {
              accounts[item.id] = {
                id: item.id,
                url: item.url,
                name: item.name,
                login: item.account_information.login.value,
                password: item.account_information.password.value,
                website_id: item.website_id,
              }
            });
            this.setState({
              importedAccounts: accounts,
              view: 4,
              error: '',
              fields: {field1: 'url', field2: 'name', field3: 'login', field4: 'password'},
              loading: false
            });
          }
        }).catch(err => {
        });
      }
    }
    else
      this.setState({view: this.state.view + 1, error: '', specialError: false, loading: false});
  };
  back = () => {
    if (this.state.passwordManager === 1)
      this.setState({view: 1, error: '', specialError: false, separator: ',',});
    else
      this.setState({view: this.state.view - 1, error: '', specialError: false});
  };
  choosePasswordManager = (int) => {
    if (int === 4)
      this.setState({
        passwordManager: int,
        fields:{
          field1: 'url',
          field2: 'login',
          field3: 'password',
          field4: 'extra',
          field5: 'name'
        }
      });
    else if (int === 5)
      this.setState({
        passwordManager: int,
        fields:{
          field1: 'password',
          field2: 'login',
          field3: 'note',
          field4: 'name',
          field5: 'type',
          field6: 'url'
        }
      });
    else if (int === 6)
      this.setState({
        passwordManager: int,
        fields:{
          field1: 'name',
          field2: 'login',
          field3: 'password',
          field4: 'url'
        }
      });
    else if (int === 8)
      this.setState({
        passwordManager: int,
        fields:{
          field1: 'name',
          field2: 'url',
          field3: 'note',
          field4: 'login',
          field5: 'password'
        }
      });
    else
      this.setState({
        passwordManager: int,
        fields: {
          field1: 'name',
          field2: 'url',
          field3: 'login',
          field4: 'password',
          field5: '-',
          field6: '-'
        }
      });
  };
  importAccounts = () => {
    this.setState({loadingSending: true});
    let calls = [];
    let trackingCalls = [];
    if (this.state.selectedProfile > 0) {
      Object.keys(this.state.accountsPending).map(app_id => {
        const app = this.state.accountsPending[app_id];
        if (app.website_id !== -1 && app.login !== '' && app.password !== '') {
          calls.push(this.props.dispatch(catalogAddClassicApp({
            name: app.name,
            website_id: app.website_id,
            profile_id: this.state.selectedProfile,
            account_information: {login: app.login, password: app.password}
          })));
          app.type = 'classicApp';
        }
        else {
          if (app.login !== '' && app.password !== '') {
            calls.push(this.props.dispatch(catalogAddAnyApp({
              name: app.name,
              url: app.url,
              img_url: app.logo,
              profile_id: this.state.selectedProfile,
              account_information: {login: app.login, password: app.password},
              connection_information: {
                login: {type: "text", priority: 0, placeholder: "Login"},
                password: {type: "password", priority: 1, placeholder: "Password"}
              },
              credentials_provided: false
            })));
            app.type = 'anyApp';
          }
          else {
            calls.push(this.props.dispatch(catalogAddBookmark({
              name: app.name,
              url: app.url,
              img_url: app.logo !== '' ? app.logo : '/resources/icons/link_app.png',
              profile_id: this.state.selectedProfile
            })));
            app.type = 'linkApp';
          }
        }
        calls.push(this.props.dispatch(deleteImportedAccount({
          id: app.id
        })));
        trackingCalls.push(this.props.dispatch(appAdded({
          app: app,
          from: "Importation"
        })))
      });
      let teams = {};
      Object.keys(this.props.teams).map(item => {
        const team = this.props.teams[item];
        teams[item] = {...team};
        teams[item].rooms = {};
        Object.keys(team.rooms).map(room_id => {
          teams[item].rooms[room_id] = {...team.rooms[room_id]}
        });
      });
      const keys = Object.keys(teams);
      const roomName = {};
      const roomAdded = {};
      keys.map(item => {
        roomName[item] = "";
        roomAdded[item] = false;
        return item;
      });
      return Promise.all(calls.map(reflect)).then(response => {
        this.setState({
          accountsPending: {},
          selectedProfile: -1,
          selectedRoom: -1,
          selectedTeam: -1,
          loadingSending: false,
          roomAdded: roomAdded,
          error: '',
          roomName: roomName,
          location: '',
          profiles: Object.assign({}, this.props.profiles),
          teamsInState: teams
        });
        if (this.state.importedAccounts.length < 1)
          this.setState({view: 1, separator: ',', specialError: false});
      }).catch(err => {
        this.setState({error: err, loadingSending: false});
      });
    }
    else if (this.state.selectedProfile === -1 && this.state.selectedRoom > 0)
      calls = this.importAccountsRoom();
    else if (this.state.selectedProfile === 0)
      calls = this.importAccountsNewProfile();
    else if (this.state.selectedRoom === 0)
      calls = this.importAccountsNewRoom();
  };
  importAccountsNewProfile = () => {
    let calls = [];
    let trackingCalls = [];
    this.props.dispatch(createProfile({
      name: 'Me',
      column_index: 1
    })).then(response => {
      Object.keys(this.state.accountsPending).map(app_id => {
        const app = this.state.accountsPending[app_id];
        if (app.website_id !== -1 && app.login !== '' && app.password !== '') {
          calls.push(this.props.dispatch(catalogAddClassicApp({
            name: app.name,
            website_id: app.website_id,
            profile_id: response.id,
            account_information: {login: app.login, password: app.password}
          })));
          app.type = 'classicApp';
        }
        else {
          if (app.login !== '' && app.password !== '') {
            calls.push(this.props.dispatch(catalogAddAnyApp({
              name: app.name,
              url: app.url,
              img_url: app.logo,
              profile_id: response.id,
              account_information: {login: app.login, password: app.password},
              connection_information: {
                login: {type: "text", priority: 0, placeholder: "Login"},
                password: {type: "password", priority: 1, placeholder: "Password"}
              },
              credentials_provided: false
            })));
            app.type = 'anyApp';
          }
          else {
            calls.push(this.props.dispatch(catalogAddBookmark({
              name: app.name,
              url: app.url,
              img_url: app.logo !== '' ? app.logo : '/resources/icons/link_app.png',
              profile_id: response.id
            })));
            app.type = 'linkApp';
          }
        }
        calls.push(this.props.dispatch(deleteImportedAccount({
          id: app.id
        })));
        trackingCalls.push(this.props.dispatch(appAdded({
          app: app,
          from: "Importation"
        })));
      });
      let newTeams = {};
      Object.keys(this.props.teams).map(item => {
        const team  = this.props.teams[item];
        newTeams[item] = {...team};
        newTeams[item].rooms = {};
        Object.keys(team.rooms).map(room_id =>  {
          newTeams[item].rooms[room_id] = {...team.rooms[room_id]}
        });
      });
      const keys = Object.keys(newTeams);
      const roomName = {};
      const roomAdded = {};
      keys.map(item => {
        roomName[item] = "";
        roomAdded[item] = false;
        return item;
      });
      Promise.all(trackingCalls);
      return Promise.all(calls.map(reflect)).then(response => {
        this.setState({
          accountsPending: {},
          selectedProfile: -1,
          selectedRoom: -1,
          selectedTeam: -1,
          loadingSending: false,
          roomAdded: roomAdded,
          error: '',
          roomName: roomName,
          location: '',
          profiles: Object.assign({}, this.props.profiles),
          teamsInState: newTeams
        });
        if (this.state.importedAccounts.length < 1)
          this.setState({view: 1, separator: ',', specialError: false});
      }).catch(err => {
        this.setState({error: err, loadingSending: false});
      });
    });
  };
  importAccountsRoom = async () => {
    let trackingCalls = [];
    const receivers = this.props.teams[this.state.selectedTeam].rooms[this.state.selectedRoom].team_user_ids.map(item => {
      return {id: item, allowed_to_see_password: this.props.teams[this.state.selectedTeam].team_users[item].role > 1}
    }).reduce((prev, curr) => {
      return {...prev, [curr.id]: {allowed_to_see_password: curr.allowed_to_see_password}}
    }, {});
    const receiversAnyApp = this.props.teams[this.state.selectedTeam].rooms[this.state.selectedRoom].team_user_ids.map(item => {
      return {id: item, allowed_to_see_password: true}
    }).reduce((prev, curr) => {
      return {...prev, [curr.id]: {allowed_to_see_password: curr.allowed_to_see_password}}
    }, {});
    const receiversLink = this.props.teams[this.state.selectedTeam].rooms[this.state.selectedRoom].team_user_ids;
    for (let i = 0; i < Object.keys(this.state.accountsPending).length; i++) {
      const app = this.state.accountsPending[Object.keys(this.state.accountsPending)[i]];
      if (app.website_id !== -1 && app.login !== '' && app.password !== '') {
        await this.props.dispatch(teamCreateSingleApp({
          team_id: this.state.selectedTeam,
          channel_id: this.state.selectedRoom,
          name: app.name,
          website_id: app.website_id,
          password_reminder_interval: 0,
          account_information: {login: app.login, password: app.password},
          description: '',
          receivers: receivers
        }));
        app.type = 'teamSingleApp';
        app.sub_type = 'classic';
      }
      else {
        if (app.login !== '' && app.password !== '') {
          await this.props.dispatch(teamCreateAnySingleCard({
            team_id: this.state.selectedTeam,
            channel_id: this.state.selectedRoom,
            name: app.name,
            description: '',
            password_reminder_interval: 0,
            url: app.url,
            img_url: app.logo,
            account_information: {login: app.login, password: app.password},
            connection_information: {
              login: {type: "text", priority: 0, placeholder: "Login"},
              password: {type: "password", priority: 1, placeholder: "Password"}
            },
            credentials_provided: false,
            receivers: receiversAnyApp
          }));
          app.type = 'teamSingleApp';
          app.sub_type = 'any';
        }
        else {
          await this.props.dispatch(teamCreateLinkCard({
            team_id: this.state.selectedTeam,
            channel_id: this.state.selectedRoom,
            name: app.name,
            description: '',
            url: app.url,
            img_url: app.logo,
            receivers: receiversLink
          }));
          app.type = 'teamLinkApp';
        }
      }
      await this.props.dispatch(deleteImportedAccount({
        id: app.id
      }));
      trackingCalls.push(this.props.dispatch(appAdded({
        app: app,
        from: "Importation"
      })))
    }
    Promise.all(trackingCalls);
    let newTeams = {};
    Object.keys(this.props.teams).map(item => {
      const team  = this.props.teams[item];
      newTeams[item] = {...team};
      newTeams[item].rooms = {};
      Object.keys(team.rooms).map(room_id =>  {
        newTeams[item].rooms[room_id] = {...team.rooms[room_id]}
      });
    });
    const keys = Object.keys(newTeams);
    const roomName = {};
    const roomAdded = {};
    keys.map(item => {
      roomName[item] = "";
      roomAdded[item] = false;
      return item;
    });
    this.setState({
      accountsPending: {},
      selectedProfile: -1,
      selectedRoom: -1,
      selectedTeam: -1,
      loadingSending: false,
      roomAdded: roomAdded,
      error: '',
      roomName: roomName,
      location: '',
      profiles: Object.assign({}, this.props.profiles),
      teamsInState: newTeams
    });
    if (Object.keys(this.state.importedAccounts).length < 1)
      this.setState({view: 1, separator: ',',})
  };
  importAccountsNewRoom = async () => {
    let trackingCalls = [];
    const receivers = {[this.props.teams[this.state.selectedTeam].my_team_user_id]: {allowed_to_see_password: true}};
    const receiversLink = [this.props.teams[this.state.selectedTeam].my_team_user_id];
    const response = await this.props.dispatch(createTeamChannel({
      team_id: this.state.selectedTeam,
      name: this.state.roomName[this.state.selectedTeam],
      purpose: ''
    }));
    const resp2 = await  this.props.dispatch(addTeamUserToChannel({
      team_id: this.state.selectedTeam,
      channel_id: response.id,
      team_user_id: this.props.teams[this.state.selectedTeam].my_team_user_id
    }));
    for (let i = 0; i < this.state.accountsPending.length; i++) {
      const app = this.state.accountsPending[Object.keys(this.state.accountsPending)[i]];
      if (app.website_id !== -1 && app.login !== '' && app.password !== '') {
        await this.props.dispatch(teamCreateSingleApp({
          team_id: this.state.selectedTeam,
          channel_id: response.id,
          name: app.name,
          website_id: app.website_id,
          password_reminder_interval: 0,
          account_information: {login: app.login, password: app.password},
          description: '',
          receivers: receivers
        }));
        app.type = 'teamSingleApp';
        app.sub_type = 'classic';
      }
      else {
        if (app.login !== '' && app.password !== '') {
          await this.props.dispatch(teamCreateAnySingleCard({
            team_id: this.state.selectedTeam,
            channel_id: response.id,
            name: app.name,
            description: '',
            password_reminder_interval: 0,
            url: app.url,
            img_url: app.logo,
            account_information: {login: app.login, password: app.password},
            connection_information: {
              login: {type: "text", priority: 0, placeholder: "Login"},
              password: {type: "password", priority: 1, placeholder: "Password"}
            },
            credentials_provided: false,
            receivers: receivers
          }));
          app.type = 'teamSingleApp';
          app.sub_type = 'any';
        }
        else {
          await this.props.dispatch(teamCreateLinkCard({
            team_id: this.state.selectedTeam,
            channel_id: response.id,
            name: app.name,
            description: '',
            url: app.url,
            img_url: app.logo,
            receivers: receiversLink
          }));
          app.type = 'teamLinkApp';
        }
      }
      await this.props.dispatch(deleteImportedAccount({
        id: app.id
      }));
      trackingCalls.push(this.props.dispatch(appAdded({
        app: app,
        from: "Importation"
      })))
    }
    Promise.all(trackingCalls);
    let newTeams = {};
    Object.keys(this.props.teams).map(item => {
      const team  = this.props.teams[item];
      newTeams[item] = {...team};
      newTeams[item].rooms = {};
      Object.keys(team.rooms).map(room_id =>  {
        newTeams[item].rooms[room_id] = {...team.rooms[room_id]}
      });
    });
    const keys = Object.keys(newTeams);
    const roomName = {};
    const roomAdded = {};
    keys.map(item => {
      roomName[item] = "";
      roomAdded[item] = false;
      return item;
    });
    this.setState({
      accountsPending: {},
      selectedProfile: -1,
      selectedRoom: -1,
      selectedTeam: -1,
      loadingSending: false,
      roomAdded: roomAdded,
      error: '',
      roomName: roomName,
      location: '',
      profiles: Object.assign({}, this.props.profiles),
      teamsInState: newTeams
    });
    if (this.state.importedAccounts.length < 1)
      this.setState({view: 1, separator: ',', specialError: false})
  };
  render() {
    return (
      <div id='importations' className={this.state.view === 4 ? 'step4' : null}>
        {(this.state.loading === true || this.props.fetching) && <Loader active />}
        {(this.state.view === 1 && this.state.loading === false && !this.props.fetching) &&
        <ChoosePasswordManager
          {...this.props}
          next={this.changeView}
          passwordManager={this.state.passwordManager}
          choosePasswordManager={this.choosePasswordManager}/>}
        {(this.state.view === 2 && this.state.passwordManager !== 2 && this.state.loading === false) &&
        <Explication
          back={this.back}
          next={this.changeView}
          passwordManager={this.state.passwordManager}/>}
        {(this.state.view === 2 && this.state.passwordManager === 2 && this.state.loading === false) &&
        <React.Fragment>
          <ChromeFirstStep
            back={this.back}
            next={this.changeView}
            onChange={this.handleInput}
            login={this.state.chromeLogin}
            selectAccount={this.selectAccount}
            password={this.state.chromePassword}
            resetChromeCredentials={this.resetChromeCredentials}/>
          <Message error hidden={this.state.error === ''} visible={this.state.error !== ''} size="mini" content={this.state.error} style={{width: "430px", left: "50%", transform: "translateX(-50%)"}}/>
          <Message hidden={!this.state.specialError} visible={this.state.specialError} negative style={{width: "430px", left: "50%", transform: "translateX(-50%)"}}>
            <p style={{color: "#eb555c"}}>☝️ No password found! Make sure your Chrome account is
              <strong> synchronized. <a target='_blank' style={{textDecoration:"underline", color: "#eb555c"}} href="https://blog.ease.space/get-the-best-of-your-chrome-importation-on-ease-space-b2f955dbf8f4">Click Here</a> </strong>
              to find how do it in few clicks.</p>
          </Message>
        </React.Fragment>}
        {(this.state.view === 3 && this.state.passwordManager !== 2 && this.state.loading === false) &&
        <PasteStep
          back={this.back}
          next={this.changeView}
          error={this.state.error}
          fields={this.state.fields}
          onChange={this.handleInput}
          pasted={this.state.paste !== ''}
          onChangeField={this.changeOrderField}
          passwordManager={this.state.passwordManager}/>}
        {(this.state.view === 3 && this.state.passwordManager === 2 && this.state.loading === false) &&
        <ChromeSecondStep
          back={this.back}
          next={this.changeView}
          error={this.state.error}/>}
        {(this.state.view === 4 && this.state.importedAccounts && !this.props.fetching && this.state.loading === false) &&
        <DisplayAccounts
          {...this.props}
          getLogo={this.getLogo}
          error={this.state.error}
          toPending={this.toPending}
          selectRoom={this.selectRoom}
          location={this.state.location}
          selectProfile={this.selectProfile}
          handleAppInfo={this.handleAppInfo}
          cancelPending={this.cancelPending}
          importAccounts={this.importAccounts}
          loadingLogo={this.state.loadingLogo}
          profilesInState={this.state.profiles}
          teamsInState={this.state.teamsInState}
          loadingSending={this.state.loadingSending}
          accountsPending={this.state.accountsPending}
          selectedProfile={this.state.selectedProfile}
          importedAccounts={this.state.importedAccounts}/>}
      </div>
    )
  }
}

export default Importations;