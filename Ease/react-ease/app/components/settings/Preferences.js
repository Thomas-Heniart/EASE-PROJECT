import React from 'react';
import { Segment, Checkbox, Header } from 'semantic-ui-react';
import {setBackgroundPicture, setHomepage} from "../../actions/commonActions";
import {reduxActionBinder} from "../../actions/index";
import extension from "../../utils/extension_api";
import {connect} from "react-redux";

@connect(store => ({
    common: store.common
}), reduxActionBinder)
class Preferences extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            homepage: this.props.common.homepage,
            background: this.props.common.user.background_picture,
            loadingHomepage: false,
            loadingBackground: false,
            errorMessage: ''
        }
    }
    toggleHomepage = () => {
        this.setState({ loadingHomepage: true });
        extension.set_homepage({
          state: !this.props.common.homepage
        }).then(response => {
          this.props.dispatch(setHomepage({
            homepage: !this.props.common.homepage
          }));
          this.setState({ loadingHomepage: false });
        }).catch(err => {
          this.setState({ loadingHomepage: false });
        });
    };
    toggleBackground = () => {
        this.setState({loadingBackground: true});
        if (this.state.background === true) {
            this.props.dispatch(setBackgroundPicture({
                active: false
            })).then(response => {
                this.setState({loadingBackground: false, errorMessage: '', background: false});
            }).catch(err => {
                this.setState({loadingBackground: false, errorMessage: err});
            });
        }
        else {
            this.props.dispatch(setBackgroundPicture({
                active: true
            })).then(response => {
                this.setState({loadingBackground: false, errorMessage: '', background: true});
            }).catch(err => {
                this.setState({loadingBackground: false, errorMessage: err});
            });
        }
    };
    render() {
        return (
            <Segment>
                <Header as='h5'>Choose your preferences</Header>
                <div>
                    <Checkbox toggle checked={this.props.common.homepage} onChange={this.toggleHomepage} disabled={this.state.loadingHomepage} />
                    <span>Ease.space as Homepage</span>
                </div>
                <p>This option allows you to have the Ease.space page when you’ll open a new tab in your browser.</p>
                <div>
                    <Checkbox toggle checked={this.state.background} onChange={this.toggleBackground} disabled={this.state.loadingBackground} />
                    <span>Daily background picture</span>
                </div>
                <p>Each day we’ll display a nice background picture, coming from <a href='https://unsplash.com' target='blank'>unsplash.com</a>, behind your Apps.</p>
            </Segment>
        )
    }
}

export default Preferences;