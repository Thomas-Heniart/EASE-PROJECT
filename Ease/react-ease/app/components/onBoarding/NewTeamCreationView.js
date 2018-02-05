import React from 'react';
import {connect} from "react-redux";
import queryString from "query-string";
import OnBoardingRooms from "./OnBoardingRooms";
import OnBoardingUsers from "./OnBoardingUsers";
import OnBoardingGroups from "./OnBoardingGroups";
import OnBoardingAccounts from "./OnBoardingAccounts";
import OnBoardingInformations from "./OnBoardingInformation";
import { Menu, Form, Icon, Button } from 'semantic-ui-react';
import {handleSemanticInput, isEmail, reflect} from "../../utils/utils";
import {withRouter, Switch, Route, NavLink} from "react-router-dom";
import {
  askRegistration, changeStep, checkAskRegistration, createTeam, createTeamProfile, editFirstNameAndLastName,
  fetchOnBoardingRooms,
  getInfoClearbit,
  newRegistration, onBoardingImportation, resetOnBoardingImportation
} from "../../actions/onBoardingActions";
import {addTeamUserToChannel, createTeamChannel} from "../../actions/channelActions";
import {teamCreateEnterpriseCard, teamCreateSingleApp} from "../../actions/appsActions";
import {processConnection, setLoginRedirectUrl} from "../../actions/commonActions";
import {testCredentials} from "../../actions/catalogActions";
import {createTeamUser} from "../../actions/userActions";
import * as api from '../../utils/api';

@connect(store => ({
  teams: store.teams,
  user: store.common.user,
  authenticated: store.common.authenticated
}))
class NewTeamCreationView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      plan_id: 0,
      sending: false,
      loading: false,
      error: '',
      activeItem: 1,
      view: 1,
      viewInfo: 1,
      email: '',
      checkEmail: false,
      confirmationCode: '',
      phone: '',
      password: '',
      verificationPassword: '',
      passwordError: true,
      phoneError: true,
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
    };
  }

  componentWillMount() {
    // get step if not let view 1
    // get Rooms
  }

  componentDidMount() {
    const query = queryString.parse(this.props.location.search);
    if ((query.plan_id !== undefined && query.plan_id.length !== 0))
      this.setState(() => ({plan_id: Number(query.plan_id)}));
    if (query.email !== undefined)
      this.setState({email: query.email});
    if (this.props.authenticated && query.team !== undefined && query.team.length !== 0)
      this.setState(() => ({plan_id: Number(query.team)}));
    if (this.props.authenticated && (query.team === undefined || query.team.length === 0))
      this.props.history.replace(`/main/simpleTeamCreation?plan_id=${this.state.plan_id}`);
    if (this.props.authenticated && (query.team !== undefined && query.team.length !== 0))
      this.props.history.replace(`/main/simpleTeamCreation?plan_id=${this.state.plan_id}&team=${query.team}`);
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
    this.setState({credentialsSingleApps: credentialsSingleApps});
  };
  selectRoom = (id) => {
    const roomsSelected = this.state.roomsSelected.filter(item => {
      if (item !== id)
        return item;
    });
    if (roomsSelected.length === this.state.roomsSelected.length)
      roomsSelected.push(id);
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
    if (Object.keys(singleApps).filter(item => {
        return singleApps[item].length > 0
      }).length === 0) {
      this.props.dispatch(changeStep({
        team_id: this.state.team_id,
        step: 5
      })).then(res => {
        easeTracker.trackEvent("EaseOnboardingEnterAccounts");
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
    return (this.state.password !== '' && this.state.password === this.state.verificationPassword && !this.state.phoneError && /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,}$/.test(this.state.password));
  };
  checkNoDuplicateEmails = () => {
    return this.state.emails.filter((item, idx) => {
      return this.state.emails.filter((email, i) => {
        return i !== idx && item.email !== '' && email.email !== '' && item.email === email.email
      }).length > 0;
    }).length > 0 || this.state.pasteEmails.filter((item, idx) => {
      return this.state.pasteEmails.filter((email, i) => {
        return i !== idx && item !== '' && email !== '' && item === email
      }).length > 0
    }).length > 0;
  };
  testPassword = (id) => {
    this.props.dispatch(testCredentials({
      account_information: {
        login: this.state.credentialsSingleApps[id].login,
        password: this.state.credentialsSingleApps[id].password
      },
      website_id: id
    }));
  };
  sentences = () => {
    if (this.state.view === 1) {
      if (this.state.viewInfo === 1)
        return <p>Confirm your email. You will receive a verification code.</p>;
      else if (this.state.viewInfo === 2)
        return <p>We’ve sent a code to <strong>{this.state.email}</strong>, it will expire shortly.<br/><br/>
          Haven’t received our email?<br/>Try our spam folder or <br/>{this.state.sending === false ?
            <u style={{cursor: 'pointer'}} onClick={this.resendEmail}>Resend email</u> : <u>Email sent</u>}</p>;
      else if (this.state.viewInfo === 3)
        return <p>Enter a <strong>strong password</strong><br/>without names, dates or info<br/>related to
          you.<br/><br/>
          Your password must contain<br/>at least <strong>8 characters,<br/>an uppercase, a lowercase<br/>and a
            number</strong>.</p>;
      else if (this.state.viewInfo === 4)
        return <p>This information will be<br/>available only to your team.</p>;
    }
    else if (this.state.view === 2)
      return <p>On Ease.space, passwords are grouped into Rooms. <strong>Rooms enable fast and organised password
        sharing</strong>.<br/><br/>
        Ex: the Marketing Room groups the marketing passwords and is accessible by the marketing team.</p>;
    else if (this.state.view === 3)
      return <p>This step <strong>will not send invitations to your team</strong>.<br/>
        Adding members allows to start the setup of your platform before you invite them. The best for you is to add
        everyone.</p>;
    else if (this.state.view === 4)
      return <p>Remember, a Room groups passwords together so people can access them easily. People can be in more than
        one room.</p>;
    else if (this.state.view === 5) {
      if (this.state.viewAccounts === 1)
        return <p>We have done our best to make your account importation seamless.<br/>Choose a tool to import or
          justmanage manually</p>;
      else if (this.state.viewAccounts === 2 && this.state.currentRoom === 0)
        return <p>There is a special Room for accounts everybody uses, it’s the #openspace. Also, if you have more than
          one account on a website, you’ll be able to add it later.</p>;
      else if (this.state.viewAccounts === 2 && this.state.currentRoom > 0)
        return <p>This is a short list of tools to get started, you’ll be able to add more accounts later.</p>;
      else if (this.state.viewAccounts === 3)
        return <p>On the company’s Twitter for example, people share the same login and password to access the
          account.</p>;
      else if (this.state.viewAccounts === 4)
        return <p>If someone else knows a password, ask him or her to fill it for everybody. Also, when you are not
          1000% sure about a password, you can test it in one click. </p>;
    }
  };
  resendEmail = () => {
    this.setState({sending: true});
    this.props.dispatch(askRegistration({
      email: this.state.email,
      newsletter: this.state.checkEmail
    })).then(response => {
      if (response.success)
        this.setState({
          firstName: response.first_name,
          lastName: response.last_name,
          companyName: response.company_name
        });
      window.setTimeout(() => {
        this.setState({sending: false})
      }, 2000);
    });
  };
  invitationsReady = () => {
    return this.state.emails.filter(item => (isEmail(item.email))).length >= (this.state.companySize == 1 ? 0 : this.state.companySize <= 5 ? 1 : this.state.companySize > 30 ? 15 : parseInt(this.state.companySize / 2 - 1))
      || this.state.pasteEmails.filter(item => (isEmail(item))).length >= (this.state.companySize == 1 ? 0 : this.state.companySize <= 5 ? 1 : this.state.companySize > 30 ? 15 : parseInt(this.state.companySize / 2 - 1));
  };
  noToolsFound = () => {
    if (this.state.roomsSelected.length - 1 > this.state.currentRoom)
      this.setState({
        currentRoom: this.state.currentRoom + 1,
        loading: false,
        appsSelected: [],
        allAppIdsSelected: this.state.allAppIdsSelected.concat(this.state.appsSelected)
      });
    else if (Object.keys(this.state.singleApps).filter(item => {
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
      easeTracker.trackEvent("EaseOnboardingChooseTools");
      this.setState({loading: false, appsSelected: [], viewAccounts: 4});
    }
    else
      this.props.dispatch(changeStep({
        team_id: this.state.team_id,
        step: 5
      })).then(res => {
        easeTracker.trackEvent("EaseOnboardingEnterAccounts");
        window.location.href = "/";
      });
  };
  noSingleCards = () => {
    let singleApps = Object.assign({}, this.state.singleApps);
    singleApps[this.state.rooms[this.state.currentRoom].id] = this.state.singleApps[this.state.rooms[this.state.currentRoom].id].filter(item => {
      return item !== item;
    });
    let calls = [];
    const users = {};
    this.state.value[this.state.rooms[this.state.currentRoom].id].map(user_id => {
      users[user_id] = {account_information: null};
    });
    this.props.dispatch(createTeamProfile({
      team_id: this.state.team_id,
      team_user_ids: [this.props.teams[this.state.team_id].my_team_user_id]
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
      if (this.state.roomsSelected.length - 1 > this.state.currentRoom) {
        Promise.all(calls.map(reflect)).then(response => {
          this.setState({
            currentRoom: this.state.currentRoom + 1,
            viewAccounts: 2,
            loading: false,
            appsSelected: [],
            singleApps: singleApps,
            allAppIdsSelected: this.state.allAppIdsSelected.concat(this.state.appsSelected)
          });
        });
      }
      else if (Object.keys(singleApps).filter(item => {
          return singleApps[item].length > 0
        }).length > 0) {
        Promise.all(calls.map(reflect)).then(response => {
          easeTracker.trackEvent("EaseOnboardingChooseTools");
          this.setState({
            loading: false,
            appsSelected: [],
            viewAccounts: 4,
            singleApps: singleApps,
          });
        });
      }
      else {
        Promise.all(calls.map(reflect)).then(response => {
          this.props.dispatch(changeStep({
            team_id: this.state.team_id,
            step: 5
          })).then(res => {
            easeTracker.trackEvent("EaseOnboardingEnterAccounts");
            window.location.href = "/";
          });
        });
      }
    });
  };
  nextInformation = () => {
    if (this.state.viewInfo === 1 && isEmail(this.state.email)) {
      this.setState({loading: true});
      this.props.dispatch(askRegistration({
        email: this.state.email,
        newsletter: this.state.checkEmail
      })).then(response => {
        if (response.success)
          this.setState({
            firstName: response.first_name,
            lastName: response.last_name,
            companyName: response.company_name
          });
        this.setState({viewInfo: 2, loading: false, error: ''});
      }).catch(err => {
        this.setState({error: err, loading: false});
      });
    }
    else if (this.state.viewInfo === 2) {
      this.setState({loading: true});
      this.props.dispatch(checkAskRegistration({
        email: this.state.email,
        digits: this.state.confirmationCode
      })).then(response => {
        if (response.valid_digits)
          this.setState({viewInfo: 3, error: '', loading: false});
      }).catch(response => {
        this.setState({error: 'This code is incorrect or expired. Try again.', loading: false});
      });
    }
    else if (this.state.viewInfo === 3) {
      this.setState({loading: true});
      // get Info and create user
      const username = this.state.email.split('@')[0];
      this.props.dispatch(newRegistration({
        username: username,
        email: this.state.email,
        password: this.state.password,
        digits: this.state.confirmationCode,
        code: '',
        phone_number: this.state.phone,
        newsletter: this.state.checkEmail,
        first_name: this.state.firstName,
        last_name: this.state.lastName
      })).then(r => {
        this.props.dispatch(processConnection({
          email: this.state.email,
          password: this.state.password
        })).then(res => {
          // this.props.dispatch(setLoginRedirectUrl('/main/simpleTeamCreation'));
          this.props.dispatch(getInfoClearbit({email: this.state.email})).then(response => {
            if (response.success)
              this.setState({
                firstName: response.first_name,
                lastName: response.last_name,
                companyName: response.company_name
              });
            this.setState({viewInfo: 4, loading: false});
          });
        });
        easeTracker.trackEvent("EaseOnboardingRegistration");
      });
    }
    else if (this.state.viewInfo === 4) {
      this.setState({loading: true});
      // request Info and create team
      const username = this.state.email.split('@')[0];
      this.props.dispatch(createTeam({
        name: this.state.companyName,
        email: this.state.email,
        username: username,
        digits: this.state.confirmationCode,
        plan_id: this.state.plan_id,
        company_size: this.state.companySize,
      })).then(response => {
        easeTracker.setUserId(this.state.email);
        easeTracker.trackEvent("EaseOnboardingInformationFilled", {
          "plan_id": this.props.plan_id
        });
        this.props.dispatch(editFirstNameAndLastName({
          first_name: this.state.firstName,
          last_name: this.state.lastName
        })).then(resp => {
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
              this.setState({
                rooms: tmp,
                roomsWebsites: onBoardingWebsites,
                activeItem: 2,
                view: 2,
                loading: false,
                team_id: response.id,
                roomsSelected: [Object.keys(response.rooms)[0]]
              });
              this.props.history.replace(`/teamCreation/rooms?team=${response.id}`);
            });
          });
        });
      }).catch(err => {
        this.setState({errorMessage: err, loading: false});
      });
    }
  };
  nextAccounts = () => {
    if (this.state.viewAccounts === 1 && isEmail(this.state.email)) {
      this.setState({loading: true});
      // Choose PM or mano
      if (this.state.passwordManagerSelected < 10) {
        easeTracker.trackEvent("EaseOnboardingImportation");
        this.props.dispatch(onBoardingImportation({
          team_id: this.state.team_id,
          passwordManager: this.state.passwordManagerSelected
        }));
        this.props.history.replace(`/main/catalog/onBoardingImportation?team=${this.state.team_id}`);
      }
      //send to reducer PM etc...
      else {
        easeTracker.trackEvent("EaseOnboardingNoImportation");
        this.setState({viewAccounts: 2, loading: false});
      }
    }
    else if (this.state.viewAccounts === 2) {
      this.setState({loading: true});
      // Choose apps for #openspace and #rooms
      if (this.state.rooms[this.state.currentRoom].name === 'openspace') {
        let calls = [];
        const users = {};
        this.state.value[this.state.rooms[this.state.currentRoom].id].map(user_id => {
          users[user_id] = {account_information: null};
        });
        this.props.dispatch(createTeamProfile({
          team_id: this.state.team_id,
          team_user_ids: [this.props.teams[this.state.team_id].my_team_user_id]
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
      else
        this.setState({viewAccounts: 3, loading: false});
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
      this.props.dispatch(createTeamProfile({
        team_id: this.state.team_id,
        team_user_ids: [this.props.teams[this.state.team_id].my_team_user_id]
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
              easeTracker.trackEvent("EaseOnboardingChooseTools");
              this.setState({viewAccounts: 4, loading: false});
            }
            else
              this.props.dispatch(changeStep({
                team_id: this.state.team_id,
                step: 5
              })).then(res => {
                easeTracker.trackEvent("EaseOnboardingEnterAccounts");
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
        this.props.dispatch(createTeamProfile({
          team_id: this.state.team_id,
          team_user_ids: [this.props.teams[this.state.team_id].my_team_user_id]
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
                receivers: receivers,
              })));
            });
          });
          Promise.all(calls.map(reflect)).then(response => {
            this.props.dispatch(changeStep({
              team_id: this.state.team_id,
              step: 5
            })).then(res => {
              easeTracker.trackEvent("EaseOnboardingEnterAccounts");
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
    if (this.state.view === 2) {
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
        this.props.dispatch(changeStep({
          team_id: this.state.team_id,
          step: 1
        })).then(res => {
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
          this.props.history.replace(`/teamCreation/users?team=${this.state.team_id}`);
          this.setState({
            loading: false,
            activeItem: 3,
            view: 3,
            emails: emails,
            rooms: rooms,
            roomsSelected: roomsSelected
          });
        });
      });
    }
    else if (this.state.view === 3) {
      // create users
      let emails = this.state.emails.filter((item, idx) => {
        return (isEmail(item.email) && (this.state.plan_id === 1 || (this.state.plan_id === 0 && idx < 29)));
      }).map(item => {
        return item.email;
      });
      if (emails.length < (this.state.companySize == 1 ? 0 : this.state.companySize <= 5 ? 1 : this.state.companySize > 30 ? 15 : parseInt(this.state.companySize / 2 - 1)))
        emails = this.state.pasteEmails.filter((item, idx) => {
          return (isEmail(item) && (this.state.plan_id === 1 || (this.state.plan_id === 0 && idx < 29)));
        });
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
          easeTracker.trackEvent("EaseOnboardingPeopleCreated");
          this.props.history.replace(`/teamCreation/groups?team=${this.state.team_id}`);
          this.setState({loading: false, activeItem: 4, view: 4, users: users, value: value});
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
          easeTracker.trackEvent("EaseOnboardingPeopleAddedInRooms");
          this.state.value[this.state.roomsSelected[0]] = this.state.users.map(user => {
            return user.id
          });
          this.state.value[this.state.roomsSelected[0]].push(this.props.teams[this.state.team_id].my_team_user_id);
          this.props.history.replace(`/teamCreation/accounts?team=${this.state.team_id}`);
          this.setState({loading: false, activeItem: 5, view: 5, singleApps: singleApps, users: users});
        });
      });
    }
  };
  render() {
    if ((this.state.view === 1 && this.props.history.location.pathname !== '/teamCreation/informations'))
      this.props.history.replace('/teamCreation/informations');
    else if ((this.state.view === 2 && (this.props.history.location.pathname !== `/teamCreation/rooms` || this.props.history.location.search !== `?team=${this.state.team_id}`)))
      this.props.history.replace(`/teamCreation/rooms?team=${this.state.team_id}`);
    else if ((this.state.view === 3 && (this.props.history.location.pathname !== `/teamCreation/users` || this.props.history.location.search !== `?team=${this.state.team_id}`)))
      this.props.history.replace(`/teamCreation/users?team=${this.state.team_id}`);
    else if ((this.state.view === 4 && (this.props.history.location.pathname !== '/teamCreation/groups' || this.props.history.location.search !== `?team=${this.state.team_id}`)))
      this.props.history.replace(`/teamCreation/groups?team=${this.state.team_id}`);
    else if ((this.state.view === 5 && (this.props.history.location.pathname !== '/teamCreation/accounts' || this.props.history.location.search !== `?team=${this.state.team_id}`)))
      this.props.history.replace(`/teamCreation/accounts?team=${this.state.team_id}`);
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
          <div id='content' className={this.state.view === 2 || this.state.view === 3 || (this.state.view === 1 && this.state.viewInfo === 4) || this.state.view === 5 ? 'stepUsers' : null}>
            <Form onSubmit={this.state.view === 1 ? this.nextInformation : this.state.view === 5 ? this.nextAccounts : this.next} error={this.state.error !== ''}>
              <Switch>
                {this.state.view === 1 &&
                <Route path={`${this.props.match.path}/informations`}
                       render={(props) =>
                         <OnBoardingInformations
                            error={this.state.error}
                            view={this.state.viewInfo}
                            email={this.state.email}
                            checkEmail={this.state.checkEmail}
                            handleInput={this.handleInput}
                            handleInputPhone={this.handleInputPhone}
                            onChangeSize={this.handleCompanySize}
                            handleConfirmationCode={this.handleConfirmationCode}
                            confirmationCode={this.state.confirmationCode}
                            phone={this.state.phone}
                            password={this.state.password}
                            verificationPassword={this.state.verificationPassword}
                            handlePasswordInput={this.handlePasswordInput}
                            passwordError={this.state.passwordError}
                            phoneError={this.state.phoneError}
                            companyName={this.state.companyName}
                            companySize={this.state.companySize}
                            firstName={this.state.firstName}
                            lastName={this.state.lastName}
                            checkCGU={this.state.checkCGU}/>}/>}
                {this.state.view === 2 &&
                <Route path={`${this.props.match.path}/rooms`}
                       render={(props) =>
                         <OnBoardingRooms
                           rooms={this.state.rooms}
                           roomsSelected={this.state.roomsSelected}
                           selectRoom={this.selectRoom}/>}/>}
                {this.state.view === 3 &&
                <Route path={`${this.props.match.path}/users`}
                       render={(props) =>
                         <OnBoardingUsers
                           onChange={this.editField}
                           emails={this.state.emails}
                           addNewField={this.addNewField}
                           textareaToEmails={this.textareaToEmails}
                           number={(this.state.companySize == 1 ? 0
                             : this.state.companySize <= 5 ? 1
                               : this.state.companySize > 30 ? 15
                                 : parseInt(this.state.companySize / 2 - 1))}/>}/>}
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
                           rooms={this.state.rooms}
                           users={this.state.users}
                           {...this.props}/>}/>}
              </Switch>
            </Form>
          </div>
          <div id='bottom_bar'>
            <Button positive
                    size='tiny'
                    type='submit'
                    loading={this.state.loading}
                    onClick={this.state.view === 1 ? this.nextInformation : this.state.view === 5 ? this.nextAccounts : this.next}
                    disabled={(this.state.loading) || (this.state.view === 1 && !isEmail(this.state.email))
                    || (this.state.viewInfo === 2 && this.state.confirmationCode.length < 6)
                    || (this.state.viewInfo === 3 && !this.checkPassword())
                    || (this.state.viewInfo === 4
                      && (!this.state.checkCGU || this.state.companyName === '' || this.state.companySize === ''
                        || this.state.firstName === '' || this.state.lastName === ''))
                    || (this.state.view === 2 && this.state.roomsSelected.length < 4)
                    || (this.state.view === 3 && (!this.invitationsReady() || this.checkNoDuplicateEmails()))
                    || (this.state.view === 5 && this.state.viewAccounts === 1 && this.state.passwordManagerSelected === 0)
                    || (this.state.view === 5 && this.state.viewAccounts === 2 && this.state.appsSelected.length === 0)
                    || (this.state.view === 5 && this.state.viewAccounts === 3 && this.state.singleApps[this.state.rooms[this.state.currentRoom].id].length === 0)}>
              Next
              <Icon name='arrow right'/>
            </Button>
            {(this.state.view === 3 && this.checkNoDuplicateEmails()) &&
            <span>We’ve detected duplicate(s), can you make sure they are not in the list anymore? </span>}
            {this.state.view === 5 && this.state.viewAccounts === 2 &&
            <a onClick={this.noToolsFound}>I can’t find my tools in that list</a>}
            {this.state.view === 5 && this.state.viewAccounts === 3 &&
            <a onClick={this.noSingleCards}>We don’t share passwords for these tools</a>}
            {(this.state.view === 5 && this.state.viewAccounts === 4 && this.state.error.length > 0) &&
            <span>Hey!! you can’t send us empty field(s) 🤚Fill them or delete the entire line(s).</span>}
          </div>
        </div>
      </div>
      </div>
    )
  }
}

export default NewTeamCreationView;