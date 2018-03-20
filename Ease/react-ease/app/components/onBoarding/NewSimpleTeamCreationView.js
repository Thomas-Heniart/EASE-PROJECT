import React from 'react';
import queryString from "query-string";
import {connect} from "react-redux";
import { Header, Input, Message, Loader } from 'semantic-ui-react';
import OnBoardingRooms from "./OnBoardingRooms";
import OnBoardingUsers from "./OnBoardingUsers";
import OnBoardingGroups from "./OnBoardingGroups";
import OnBoardingAccounts from "./OnBoardingAccounts";
import { Menu, Form, Icon, Button } from 'semantic-ui-react';
import {handleSemanticInput, isEmail, reflect} from "../../utils/utils";
import {Switch, Route} from "react-router-dom";
import {
  changeStep, createTeam, createTeamProfile, fetchOnBoardingRooms, goToOnBoarding, onBoardingImportation
} from "../../actions/onBoardingActions";
import {addTeamUserToChannel, createTeamChannel} from "../../actions/channelActions";
import {teamCreateEnterpriseCard, teamCreateSingleApp} from "../../actions/appsActions";
import {testCredentials} from "../../actions/catalogActions";
import {createTeamUser} from "../../actions/userActions";
import * as api from '../../utils/api';
import ReactGA from 'react-ga';

class InformationCompany extends React.Component {
  render() {
    const {
      error,
      onChange,
      onChangeSize,
      handleInputPhone,
      companyName,
      companySize,
      phoneError,
      phone
    } = this.props;
    return (
      <React.Fragment>
        <Header as='h1'>Account information</Header>
        <div style={{display:'inline-flex'}}>
          <div style={{marginRight:'10px'}}>
            <label className='for_input'>Company name</label>
            <Input
              required
              autoFocus
              id='companyName'
              name='companyName'
              value={companyName}
              placeholder='SpaceX'
              onChange={onChange}/>
          </div>
          <div style={{marginRight:'10px'}}>
            <label className='for_input'>Company size</label>
            <Input
              required
              id='companySize'
              placeholder='65'
              name='companySize'
              value={companySize}
              onChange={onChangeSize}/>
          </div>
        </div>
        {this.props.user.phone_number === '' &&
        <div style={{display:'inline-flex'}}>
          <div style={{marginRight:'10px'}}>
            <label className='for_input'>Your Mobile phone</label>
            <Input
              required
              id='phone'
              name='phone'
              value={phone}
              placeholder='+33'
              icon='check circle'
              onChange={handleInputPhone}
              className={!phoneError ? 'password_verified' : null}/>
          </div>
        </div>}
        <Message error content={error}/>
      </React.Fragment>
    )
  }
}


@connect((store)=> ({
  user: store.common.user,
  teams: store.teams,
  onBoarding: store.onBoarding
}))
class NewSimpleTeamCreationView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      email: '',
      plan_id: 0,
      firstLoading: false,
      phoneError: this.props.user.phone_number === '',
      loading: false,
      error: '',
      activeItem: 1,
      view: 1,
      checkEmail: false,
      confirmationCode: '',
      phone: '',
      password: '',
      verificationPassword: '',
      passwordError: true,
      companyName: '',
      companySize: '',
      firstName: '',
      lastName: '',
      checkCGU: false,
      team_id: 0,
      rooms: [],
      roomsSelected: [],
      emails: [],
      pasteEmails: [],
      users: [],
      viewAccounts: 1,
      currentRoom: 0,
      passwordManagerSelected: 0,
      roomsWebsites: {},
      appsSelected: [],
      value: {},
      singleApps: {},
      credentialsSingleApps: {},
      allAppIdsSelected: []
    }
  }
  componentWillMount() {
    const query = queryString.parse(this.props.location.search);
    if ((query.plan_id !== undefined && query.plan_id.length !== 0))
      this.setState(() => ({plan_id: Number(query.plan_id)}));
    if (query.team !== undefined && query.team.length !== 0) {
      if (this.props.teams[query.team].onboarding_step < 5) {
        this.props.dispatch(goToOnBoarding({
          team_id: Number(query.team)
        }));
      }
      else
        window.location.href = '/';
    }
  }
  componentDidMount(){
    const query = queryString.parse(this.props.location.search);
    if (this.props.onBoarding.team_id || (query.team !== undefined && query.team.length !== 0)) {
      this.setState({firstLoading: true});
      let view = 2;
      this.props.dispatch(fetchOnBoardingRooms()).then(r => {
        let roomsSelected = [];
        let value = {};
        let singleApps = {};
        let rooms = Object.keys(this.props.teams[this.props.onBoarding.team_id].rooms).map(item => {
          roomsSelected.push(this.props.teams[this.props.onBoarding.team_id].rooms[item].id);
          r.map(room => {
            if (room.name === this.props.teams[this.props.onBoarding.team_id].rooms[item].name)
              this.props.teams[this.props.onBoarding.team_id].rooms[item].website_ids = room.website_ids
          });
          singleApps[item] = [];
          value[item] = this.props.teams[this.props.onBoarding.team_id].rooms[item].team_user_ids;
          return this.props.teams[this.props.onBoarding.team_id].rooms[item]
        });
        const tmp = r.map(item => {if (item.name === 'openspace'){item.id = Object.keys(this.props.teams[this.props.onBoarding.team_id].rooms)[0].id;} return item;
        }).sort((a, b) => {
          if (b.name === 'openspace')
            return 1;
          if (a.name === 'openspace')
            return -1;
          return 0;
        });
        let emails = [{email: '', error: ''}];
        if (this.props.teams[this.props.onBoarding.team_id].company_size <= 20)
          for (let i = 1; this.props.teams[this.props.onBoarding.team_id].company_size - 1 > i; i++)
            emails.push({email: '', error: ''});
        else
          for (let i = 1; 20 > i; i++)
            emails.push({email: '', error: ''});
        api.catalog.getWebsites().then(res => {
          const websites = res.websites.reduce((prev, curr) => {
            return {...prev, [curr.id]: {...curr}}
          }, {});
          const onBoardingWebsites = {};
          r.map(room => {
            room.website_ids.map(id => {
              onBoardingWebsites[id] = websites[id]
            })
          });
          if (this.props.teams[this.props.onBoarding.team_id].onboarding_step === 1)
            view = 3;
          else if (this.props.teams[this.props.onBoarding.team_id].onboarding_step === 2)
            view = 4;
          else if (this.props.teams[this.props.onBoarding.team_id].onboarding_step === 3)
            view = 5;
          else if (this.props.teams[this.props.onBoarding.team_id].onboarding_step === 4) {
            this.props.dispatch(onBoardingImportation({
              team_id: this.props.onBoarding.team_id,
              passwordManager: -1
            }));
            this.props.history.replace(`/main/catalog/onBoardingImportation?team=${this.props.onBoarding.team_id}`);
          }
          this.setState({
            firstLoading: false,
            team_id: Number(this.props.onBoarding.team_id),
            email: this.props.user.email,
            view: view,
            companySize: this.props.teams[this.props.onBoarding.team_id].company_size,
            activeItem: view,
            passwordError: false,
            firstName: this.props.user.first_name,
            lastName: this.props.user.last_name,
            checkCGU: true,
            rooms: rooms.length > 1 ? rooms : tmp,
            roomsSelected: roomsSelected,
            users: Object.keys(this.props.teams[this.props.onBoarding.team_id].team_users
            ).filter(item => {return item !== this.props.teams[this.props.onBoarding.team_id].my_team_user_id}).map(item => {
              return this.props.teams[this.props.onBoarding.team_id].team_users[item]
            }),
            roomsWebsites: onBoardingWebsites,
            emails: emails,
            value: value,
            singleApps: singleApps
          });
        });
      });
    }
    if (this.props.user !== null){
      this.setState({email: this.props.user.email, username: this.props.user.first_name});
    }
  }
  handleInput = handleSemanticInput.bind(this);
  handleConfirmationCode = (e, {name, value}) => {
    if (value.match(/^[0-9]{0,6}$/g))
      this.setState({[name]: value});
  };
  handleInputPhone = (e, {name, value}) => {
    if (/^(\+|[0-9])(?:[0-9] ?){5,13}[0-9]$/.test(value))
      this.setState({[name]: value, phoneError: false});
    else
      this.setState({[name]: value, phoneError: true});
  };
  handlePasswordInput = (e, {name, value}) => {
    if (/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/.test(value))
      this.setState({[name]: value, passwordError: false});
    else
      this.setState({[name]: value, passwordError: true});
  };
  handleCompanySize = (e, {name, value}) => {
    if (value.match(/^\d+$/) || value === '')
      this.setState({[name]: value, error: ''});
    else
      this.setState({error: 'We’ve never seen companies with less than 1 person in it! Are you sure about your company size?'});
  };
  handleAppInfo = (id, {name, value}) => {
    const credentialsSingleApps = Object.assign({}, this.state.credentialsSingleApps);
    credentialsSingleApps[id][name] = value;
    this.setState({credentialsSingleApps: credentialsSingleApps, error: ''});
  };
  selectRoom = (id) => {
    const roomsSelected = this.state.roomsSelected.filter(item => {
      if (item !== id)
        return item;
    });
    if (roomsSelected.length === this.state.roomsSelected.length && this.state.roomsSelected.length < 7)
      roomsSelected.push(id);
    else if (roomsSelected.length === this.state.roomsSelected.length && this.state.roomsSelected.length > 6)
      this.setState({error: 'Please start with 6 rooms maximum. You’ll be able to add more later!'});
    else if (roomsSelected.length !== this.state.roomsSelected.length && this.state.roomsSelected.length <= 7)
      this.setState({error: ''});
    this.setState({roomsSelected: roomsSelected});
  };
  selectPasswordManager = (id) => {
    this.setState({passwordManagerSelected: id});
  };
  selectApp = (id) => {
    const appsSelected = this.state.appsSelected.filter(item => {
      if (item !== id)
        return item;
    });
    if (appsSelected.length === this.state.appsSelected.length)
      appsSelected.push(id);
    this.setState({appsSelected: appsSelected});
  };
  selectSingleApp = (id, room_id) => {
    let singleApps = Object.assign({}, this.state.singleApps);
    singleApps[room_id] = this.state.singleApps[room_id].filter(item => {
      if (item !== id)
        return item;
    });
    if (singleApps[room_id].length === this.state.singleApps[room_id].length)
      singleApps[room_id].push(id);
    this.setState({singleApps: singleApps});
  };
  deleteSingleApp = (room_id, id) => {
    let credentialsSingleApps = {};
    Object.keys(this.state.credentialsSingleApps).map(item => {
      if (Number(item) !== id)
        credentialsSingleApps[item] = this.state.credentialsSingleApps[item];
    });
    const singleApps = Object.assign({}, this.state.singleApps);
    singleApps[room_id] = singleApps[room_id].filter(item => {
      if (item !== id)
        return item;
    });
    this.setState({singleApps: singleApps, credentialsSingleApps: credentialsSingleApps});
    if (Object.keys(singleApps).filter(item => {return singleApps[item].length > 0}).length === 0) {
      this.props.dispatch(changeStep({
        team_id: this.state.team_id,
        step: 5
      })).then(res => {
        ReactGA.event({
          category: 'form',
          action: 'createTeam'
        });
        window.location.href = "/";
      });
    }
  };
  deleteFillerId = (id) => {
    const credentialsSingleApps = Object.assign({}, this.state.credentialsSingleApps);
    credentialsSingleApps[id].filler_id = null;
    this.setState({credentialsSingleApps: credentialsSingleApps});
  };
  dropdownChange = (e, {id, value}) => {
    const newValue = Object.assign({}, this.state.value);
    newValue[id] = value;
    this.setState({value: newValue});
  };
  editField = (idx, {name, value}) => {
    let emails = this.state.emails.slice();
    emails[idx][name] = value;
    this.setState(() => ({emails: emails}));
  };
  addNewField = () => {
    const emails = this.state.emails.slice();
    emails.push({email: '', error: ''});
    this.setState({emails: emails});
  };
  textareaToEmails = (emails) => {
    this.setState({pasteEmails: emails});
  };
  checkPassword = () => {
    return (this.state.companyName !== '' && this.state.companySize !== '' && this.state.companySize !== '0' && !this.state.phoneError);
  };
  checkNoDuplicateEmails = () => {
    return this.state.emails.filter((item, idx) => {
      return this.state.emails.filter((email, i) => {return i !== idx && item.email !== '' && email.email !== '' && item.email === email.email}).length > 0;
    }).length > 0 || this.state.pasteEmails.filter((item, idx) => {
      return this.state.pasteEmails.filter((email, i) => {return i !== idx && item !== '' && email !== '' && item === email}).length > 0
    }).length > 0;
  };
  testPassword = (id) => {
    if (this.state.credentialsSingleApps[id].login !== ''
      && this.state.credentialsSingleApps[id].password !== ''
      && this.state.credentialsSingleApps[id].filler_id === null) {
      this.props.dispatch(testCredentials({
        account_information: {
          login: this.state.credentialsSingleApps[id].login,
          password: this.state.credentialsSingleApps[id].password
        },
        website_id: id
      }));
      this.setState({error: ''});
    }
    else if (this.state.credentialsSingleApps[id].password === '' && this.state.credentialsSingleApps[id].login === '')
      this.setState({error: `${id}all`});
    else if (this.state.credentialsSingleApps[id].login === '')
      this.setState({error: `${id}login`});
    else if (this.state.credentialsSingleApps[id].password === '')
      this.setState({error: `${id}password`});
  };
  sentences = () => {
    if (this.state.view === 1)
      return <p>This information will be available only to your team.</p>;
    else if (this.state.view === 2)
      return  <p>Rooms: On Ease.space, <strong>Rooms group passwords to enable easy sharing.</strong>.<br/><br/>
        Ex: #marketing groups the passwords of the marketing team</p>;
    else if (this.state.view === 3)
      return <p>Add your team now so you can decide who has access to what passwords.</p>;
    else if (this.state.view === 4)
      return <p>Remember, a Room groups passwords together so people can access them easily. People can be in more than one room.</p>;
    else if (this.state.view === 5) {
      if (this.state.viewAccounts === 1)
        return <p>We have done our best to make your account importation seamless.<br/>Choose a tool to import or just manage manually.</p>;
      else if (this.state.viewAccounts === 2 && this.state.currentRoom === 0)
        return <p>Is there tools all your team uses?<br/>If yes, they will be in a special Room called #openspace</p>;
      else if (this.state.viewAccounts === 2 && this.state.currentRoom > 0)
        return <p>This is a short list, you can add more tools later.</p>;
      else if (this.state.viewAccounts === 3)
        return <p>Ex: Most companies only have 1 twitter account they all share.</p>;
      else if (this.state.viewAccounts === 4)
        return <p>If someone else knows a password, ask him or her to fill it for everybody. Also, when you are not 1000% sure about a password, you can test it in one click. </p>;
    }
  };
  invitationsReady = () => {
    return this.state.emails.filter(item => (isEmail(item.email))).length >= (this.state.companySize == 1 ? 0 : this.state.companySize <= 5 ? 1 : this.state.companySize > 30 ? 15 : parseInt(this.state.companySize / 2 - 1))
      || this.state.pasteEmails.filter(item => (isEmail(item))).length >= (this.state.companySize == 1 ? 0 : this.state.companySize <= 5 ? 1 : this.state.companySize > 30 ? 15 : parseInt(this.state.companySize / 2 - 1));
  };
  nextAccounts = () => {
    if (this.state.viewAccounts === 1 && isEmail(this.state.email)) {
      this.setState({loading: true});
      // Choose PM or mano
      if (this.state.passwordManagerSelected < 10) {
        this.props.dispatch(onBoardingImportation({
          team_id: this.state.team_id,
          passwordManager: this.state.passwordManagerSelected
        }));
        this.props.history.replace(`/main/catalog/onBoardingImportation?team=${this.state.team_id}`);
      }
      //send to reducer PM etc...
      else {
        this.setState({viewAccounts: 2, loading: false});
      }
    }
    else if (this.state.viewAccounts === 2) {
      this.setState({loading: true});
      // Choose apps for #openspace and #rooms
      if (this.state.rooms[this.state.currentRoom].name === 'openspace' && this.state.appsSelected.length !== 0) {
        let calls = [];
        const users = {};
        this.state.value[this.state.rooms[this.state.currentRoom].id].map(user_id => {
          users[user_id] = {account_information: null};
        });
        let team_users_and_channels = {};
        team_users_and_channels[this.props.teams[this.state.team_id].my_team_user_id] = [this.state.rooms[this.state.currentRoom].id];
        this.props.dispatch(createTeamProfile({
          team_id: this.state.team_id,
          team_users_and_channels: team_users_and_channels
        })).then(res => {
          this.state.appsSelected.map(app_id => {
            calls.push(this.props.dispatch(teamCreateEnterpriseCard({
              team_id: this.state.team_id,
              channel_id: this.state.rooms[this.state.currentRoom].id,
              website_id: app_id,
              name: this.state.roomsWebsites[app_id].name,
              description: '',
              password_reminder_interval: 0,
              receivers: users
            })));
          });
          Promise.all(calls.map(reflect)).then(response => {
            this.setState({
              currentRoom: 1,
              loading: false,
              appsSelected: [],
              allAppIdsSelected: this.state.allAppIdsSelected.concat(this.state.appsSelected)
            });
          });
        });
      }
      else if (this.state.appsSelected.length === 0 && this.state.roomsSelected.length - 1 > this.state.currentRoom)
        this.setState({currentRoom: this.state.currentRoom + 1, viewAccounts: 2, loading: false});
      else if (this.state.appsSelected.length === 0
        && this.state.roomsSelected.length - 1 <= this.state.currentRoom
        && Object.keys(this.state.singleApps).filter(item => {
          return this.state.singleApps[item].length > 0
        }).length > 0) {
        Object.keys(this.state.singleApps).map(room_id => {
          this.state.singleApps[room_id].map(id => {
            this.state.credentialsSingleApps[id] = {
              name: this.state.roomsWebsites[id].name,
              login: '',
              password: '',
              filler_id: null
            };
          });
        });
        this.setState({loading: false, appsSelected: [], viewAccounts: 4})
      }
      else if (this.state.appsSelected.length > 0)
        this.setState({viewAccounts: 3, loading: false});
      else
        this.props.dispatch(changeStep({
          team_id: this.state.team_id,
          step: 5
        })).then(res => {
          ReactGA.event({
            category: 'form',
            action: 'createTeam'
          });
          window.location.href = "/";
        });
    }
    else if (this.state.viewAccounts === 3) {
      this.setState({loading: true});
      // Choose if single or enterprise / create enterpriseCard
      let calls = [];
      const enterpriseApp = this.state.appsSelected.filter(app_id => {
        return this.state.singleApps[this.state.rooms[this.state.currentRoom].id].filter(single_id => {
          return app_id === single_id
        }).length === 0
      });
      const users = {};
      this.state.value[this.state.rooms[this.state.currentRoom].id].map(user_id => {
        users[user_id] = {account_information: null};
      });
      let team_users_and_channels = {};
      team_users_and_channels[this.props.teams[this.state.team_id].my_team_user_id] = [this.state.rooms[this.state.currentRoom].id];
      this.props.dispatch(createTeamProfile({
        team_id: this.state.team_id,
        team_users_and_channels: team_users_and_channels
      })).then(res => {
        enterpriseApp.map(app_id => {
          calls.push(this.props.dispatch(teamCreateEnterpriseCard({
            team_id: this.state.team_id,
            channel_id: this.state.rooms[this.state.currentRoom].id,
            website_id: app_id,
            name: this.state.roomsWebsites[app_id].name,
            description: '',
            password_reminder_interval: 0,
            receivers: users
          })));
        });
        if (this.state.roomsSelected.length - 1 > this.state.currentRoom) {
          Promise.all(calls.map(reflect)).then(response => {
            this.setState({
              currentRoom: this.state.currentRoom + 1,
              viewAccounts: 2,
              loading: false,
              appsSelected: [],
              allAppIdsSelected: this.state.allAppIdsSelected.concat(this.state.appsSelected)
            });
          });
        }
        else {
          Promise.all(calls.map(reflect)).then(response => {
            Object.keys(this.state.singleApps).map(room_id => {
              this.state.singleApps[room_id].map(id => {
                this.state.credentialsSingleApps[id] = {
                  name: this.state.roomsWebsites[id].name,
                  login: '',
                  password: '',
                  filler_id: null
                };
              });
            });
            if (Object.keys(this.state.singleApps).filter(item => {
                return this.state.singleApps[item].length > 0
              }).length > 0) {
              this.setState({viewAccounts: 4, loading: false});
            }
            else
              this.props.dispatch(changeStep({
                team_id: this.state.team_id,
                step: 5
              })).then(res => {
                ReactGA.event({
                  category: 'form',
                  action: 'createTeam'
                });
                window.location.href = "/";
              });
          });
        }
      });
    }
    else if (this.state.viewAccounts === 4) {
      this.setState({loading: true});
      // Send creds and done
      if (Object.keys(this.state.credentialsSingleApps).filter(id => {
          return ((this.state.credentialsSingleApps[id].login === '' || this.state.credentialsSingleApps[id].password === '')
            && (this.state.credentialsSingleApps[id].filler_id === null))
        }).length === 0) {
        let calls = [];
        let team_users_and_channels = {};
        team_users_and_channels[this.props.teams[this.state.team_id].my_team_user_id] = Object.keys(this.state.singleApps);
        this.props.dispatch(createTeamProfile({
          team_id: this.state.team_id,
          team_users_and_channels: team_users_and_channels
        })).then(res => {
          Object.keys(this.state.singleApps).map(room_id => {
            const receivers = {};
            this.state.value[room_id].map(id => {
              if (id === this.props.teams[this.state.team_id].my_team_user_id)
                receivers[id] = {allowed_to_see_password: true};
              else
                receivers[id] = {allowed_to_see_password: false};
            });
            this.state.singleApps[room_id].map(app_id => {
              calls.push(this.props.dispatch(teamCreateSingleApp({
                team_id: this.state.team_id,
                channel_id: room_id,
                website_id: app_id,
                name: this.state.credentialsSingleApps[app_id].name,
                description: '',
                password_reminder_interval: 0,
                team_user_filler_id: this.state.credentialsSingleApps[app_id].filler_id,
                account_information: this.state.credentialsSingleApps[app_id].login !== '' && this.state.credentialsSingleApps[app_id].password !== '' ? {
                  login: this.state.credentialsSingleApps[app_id].login,
                  password: this.state.credentialsSingleApps[app_id].password
                } : {},
                receivers: receivers
              })));
            });
          });
          Promise.all(calls.map(reflect)).then(response => {
            this.props.dispatch(changeStep({
              team_id: this.state.team_id,
              step: 5
            })).then(res => {
              ReactGA.event({
                category: 'form',
                action: 'createTeam'
              });
              window.location.href = "/";
            });
          });
        });
      }
      else
        this.setState({loading: false, error: 'true'})
    }
  };
  next = () => {
    this.setState({loading: true});
    if (this.state.view === 1) {
      const username = this.state.email.split('@')[0];
      this.props.dispatch(createTeam({
        name: this.state.companyName,
        email: this.state.email,
        username: username,
        digits: null,
        plan_id: this.state.plan_id,
        company_size: this.state.companySize,
      })).then(response => {
        this.props.dispatch(fetchOnBoardingRooms()).then(r => {
          const tmp = r.map(item => {
            if (item.name === 'openspace') {
              item.id = Object.keys(response.rooms)[0];
            }
            return item;
          }).sort((a, b) => {
            if (b.name === 'openspace')
              return 1;
            if (a.name === 'openspace')
              return -1;
            return 0;
          });
          api.catalog.getWebsites().then(res => {
            const websites = res.websites.reduce((prev, curr) => {
              return {...prev, [curr.id]: {...curr}}
            }, {});
            const onBoardingWebsites = {};
            r.map(room => {
              room.website_ids.map(id => {
                onBoardingWebsites[id] = websites[id]
              })
            });
            this.props.history.replace(`/main/simpleTeamCreation/rooms?team=${response.id}`);
            this.setState({
              rooms: tmp,
              roomsWebsites: onBoardingWebsites,
              activeItem: 2,
              view: 2,
              error: '',
              loading: false,
              team_id: response.id,
              roomsSelected: [Object.keys(response.rooms)[0]]
            })
          });
        });
      });
    }
    else if (this.state.view === 2) {
      // request create rooms + save step + generate input email
      let calls = [];
      this.state.roomsSelected.map((id, idx) => {
        if (idx !== 0) {
          calls.push(this.props.dispatch(createTeamChannel({
            team_id: this.state.team_id,
            name: this.state.rooms.filter(item => {return item.id === id})[0].name,
            purpose: ''
          })));
        }
      });
      let emails = [{email: '', error: ''}];
      if (this.state.companySize <= 20)
        for (let i = 1; this.state.companySize - 1 > i; i++)
          emails.push({email: '', error: ''});
      else
        for (let i = 1; 20 > i; i++)
          emails.push({email: '', error: ''});
      Promise.all(calls.map(reflect)).then(response => {
        let roomsSelected = [this.state.roomsSelected[0]];
        const rooms = [this.state.rooms[0]];
        response.map(item => {
          this.state.rooms.filter(fil => {
            return fil.name === item.data.name;
          }).map(room => {
            roomsSelected.push(item.data.id);
            room.id = item.data.id;
            rooms.push(room);
            return room;
          });
        });
        this.props.history.replace(`/main/simpleTeamCreation/users?team=${this.state.team_id}`);
        this.setState({loading: false, activeItem: 3, view: 3, error: '', emails: emails, rooms: rooms, roomsSelected: roomsSelected});
      });
    }
    else if (this.state.view === 3) {
      // create users
      let emails = this.state.emails.filter(item => {
        return (isEmail(item.email));
      }).map(item => {
        return item.email;
      });
      if (emails.length < (this.state.companySize == 1 ? 0 : this.state.companySize <= 5 ? 1 : this.state.companySize > 30 ? 15 : parseInt(this.state.companySize / 2 - 1))) {
        emails = this.state.pasteEmails.filter(item => {
          return isEmail(item);
        });
      }
      let calls = emails.map(item => {
        return this.props.dispatch(createTeamUser({
          team_id: this.state.team_id,
          first_name: '',
          last_name: '',
          email: item,
          username: '',
          departure_date: null,
          role: 1
        }));
      });
      Promise.all(calls.map(reflect)).then(response => {
        this.props.dispatch(changeStep({
          team_id: this.state.team_id,
          step: 2
        })).then(res => {
          const users = response.filter(item => {
            return item.data.email;
          }).map(item => {
            return item.data;
          });
          let value = {};
          this.state.rooms.map(item => {
            if (item.name !== 'openspace')
              value[item.id] = [this.props.teams[this.state.team_id].my_team_user_id];
          });
          this.props.history.replace(`/main/simpleTeamCreation/groups?team=${this.state.team_id}`);
          this.setState({loading: false, activeItem: 4, view: 4, error: '', users: users, value: value});
        });
      });
    }
    else if (this.state.view === 4) {
      let calls = [];
      let singleApps = {};
      let users = this.state.users.slice();
      this.state.roomsSelected.map((room_id, idx) => {
        singleApps[room_id] = [];
        if (idx !== 0) {
          this.state.value[room_id].map(user_id => {
            calls.push(this.props.dispatch(addTeamUserToChannel({
              team_id: this.state.team_id,
              channel_id: room_id,
              team_user_id: user_id
            })));
            users.map(item => {
              if (user_id === item.id)
                item.room_ids.push(room_id)
            })
          });
        }
      });
      Promise.all(calls.map(reflect)).then(response => {
        this.props.dispatch(changeStep({
          team_id: this.state.team_id,
          step: 3
        })).then(res => {
          this.state.value[this.state.roomsSelected[0]] = this.state.users.map(user => {
            return user.id;
          });
          this.state.value[this.state.roomsSelected[0]].push(this.props.teams[this.state.team_id].my_team_user_id);
          this.props.history.replace(`/main/simpleTeamCreation/accounts?team=${this.state.team_id}`);
          this.setState({loading: false, activeItem: 5, view: 5, error: '', singleApps: singleApps, users: users});
        });
      });
    }
  };
  render() {
    if ((this.state.view === 1 && this.props.history.location.pathname !== '/main/simpleTeamCreation/informations'))
      this.props.history.replace('/main/simpleTeamCreation/informations');
    else if ((this.state.view === 2 && (this.props.history.location.pathname !== '/main/simpleTeamCreation/rooms' || this.props.history.location.search !== `?team=${this.state.team_id}`)))
      this.props.history.replace(`/main/simpleTeamCreation/rooms?team=${this.state.team_id}`);
    else if ((this.state.view === 3 && (this.props.history.location.pathname !== '/main/simpleTeamCreation/users' || this.props.history.location.search !== `?team=${this.state.team_id}`)))
      this.props.history.replace(`/main/simpleTeamCreation/users?team=${this.state.team_id}`);
    else if ((this.state.view === 4 && (this.props.history.location.pathname !== '/main/simpleTeamCreation/groups' || this.props.history.location.search !== `?team=${this.state.team_id}`)))
      this.props.history.replace(`/main/simpleTeamCreation/groups?team=${this.state.team_id}`);
    else if ((this.state.view === 5 && (this.props.history.location.pathname !== '/main/simpleTeamCreation/accounts' || this.props.history.location.search !== `?team=${this.state.team_id}`)))
      this.props.history.replace(`/main/simpleTeamCreation/accounts?team=${this.state.team_id}`);
    const firstP = this.sentences();
    return (
      <div className='on_boarding_wrapper'>
        <div id='new_team_creation'>
          <div id='left_bar'>
            {firstP}
            <img src='/resources/images/ease_logo_white.svg'/>
          </div>
          <div id='center'>
            <Menu id='top_bar' pointing secondary fluid>
              <Menu.Item name='Information' active={this.state.activeItem === 1}/>
              <Menu.Item name='Rooms' active={this.state.activeItem === 2}/>
              <Menu.Item name='Members' active={this.state.activeItem === 3}/>
              <Menu.Item name='Groups' active={this.state.activeItem === 4}/>
              <Menu.Item name='Accounts' active={this.state.activeItem === 5}/>
            </Menu>
            <div id='content' className={this.state.view === 2 || this.state.view === 3 || this.state.view === 1 || this.state.view === 5 ? 'stepUsers' : null}>
              <Loader active={this.state.firstLoading} inline='centered'/>
              {!this.state.firstLoading &&
              <Form
                className={this.state.view === 3 ? 'addMembers' : null}
                onSubmit={this.state.view === 5 ? this.nextAccounts : this.next} error={this.state.error !== '' || this.checkNoDuplicateEmails()}>
                <Switch>
                  {this.state.view === 1 &&
                  <Route path={`${this.props.match.path}/informations`}
                         render={(props) =>
                           <InformationCompany
                             {...this.props}
                             error={this.state.error}
                             phoneError={this.state.phoneError}
                             onChange={this.handleInput}
                             handleInputPhone={this.handleInputPhone}
                             onChangeSize={this.handleCompanySize}
                             companyName={this.state.companyName}
                             companySize={this.state.companySize}
                             phone={this.state.phone}/>}/>}
                  {this.state.view === 2 &&
                  <Route path={`${this.props.match.path}/rooms`}
                         render={(props) =>
                           <OnBoardingRooms
                             error={this.state.error}
                             rooms={this.state.rooms}
                             roomsSelected={this.state.roomsSelected}
                             selectRoom={this.selectRoom}/>}/>}
                  {this.state.view === 3 &&
                  <Route path={`${this.props.match.path}/users`}
                         render={(props) =>
                           <OnBoardingUsers
                             error={this.state.error}
                             onChange={this.editField}
                             emails={this.state.emails}
                             addNewField={this.addNewField}
                             textareaToEmails={this.textareaToEmails}
                             checkDuplicateEmails={this.checkNoDuplicateEmails}
                             number={((this.state.companySize == 1) ? 0
                               : (this.state.companySize <= 5) ? 1
                                 : (this.state.companySize > 30) ? 15
                                   : parseInt(this.state.companySize / 2 - 1, 10))}/>}/>}
                  {this.state.view === 4 &&
                  <Route path={`${this.props.match.path}/groups`}
                         render={(props) =>
                           <OnBoardingGroups
                             value={this.state.value}
                             users={this.state.users}
                             rooms={this.state.rooms}
                             dropdownChange={this.dropdownChange}
                             team={this.props.teams[this.state.team_id]}
                             roomsSelected={this.state.roomsSelected}/>}/>}
                  {this.state.view === 5 &&
                  <Route path={`${this.props.match.path}/accounts`}
                         render={(props) =>
                           <OnBoardingAccounts
                             passwordManagerSelected={this.state.passwordManagerSelected}
                             credentialsSingleApps={this.state.credentialsSingleApps}
                             selectPasswordManager={this.selectPasswordManager}
                             allAppIdsSelected={this.state.allAppIdsSelected}
                             team={this.props.teams[this.state.team_id]}
                             roomsWebsites={this.state.roomsWebsites}
                             roomsSelected={this.state.roomsSelected}
                             selectSingleApp={this.selectSingleApp}
                             deleteSingleApp={this.deleteSingleApp}
                             appsSelected={this.state.appsSelected}
                             deleteFillerId={this.deleteFillerId}
                             currentRoom={this.state.currentRoom}
                             singleApps={this.state.singleApps}
                             handleAppInfo={this.handleAppInfo}
                             testPassword={this.testPassword}
                             view={this.state.viewAccounts}
                             selectApp={this.selectApp}
                             error={this.state.error}
                             rooms={this.state.rooms}
                             users={this.state.users}
                             {...this.props}/>}/>}
                </Switch>
              </Form>}
            </div>
            <div id='bottom_bar'>
              <Button positive
                      size='tiny'
                      type='submit'
                      loading={this.state.loading}
                      onClick={this.state.view === 5 ? this.nextAccounts : this.next}
                      disabled={(this.state.loading)
                      || (this.state.view === 1 && !this.checkPassword())
                      || (this.state.view === 2 && this.state.roomsSelected.length < 3)
                      || (this.state.view === 3 && (!this.invitationsReady() || this.checkNoDuplicateEmails()))
                      || (this.state.view === 5 && this.state.viewAccounts === 1 && this.state.passwordManagerSelected === 0)}>
                Next
                <Icon name='arrow right'/>
              </Button>
              {this.state.view === 3 &&
              <a>It will not send invitations.</a>}
              {(this.state.view === 5 && this.state.viewAccounts === 4 && this.state.error === 'true') &&
              <span>Hey!! you can’t send us empty field(s) 🤚 Fill them or delete the entire line(s).</span>}
            </div>
          </div>
        </div>
      </div>
    )
  }
}

module.exports = NewSimpleTeamCreationView;