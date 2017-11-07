import React from 'react';
import { Segment, Checkbox, Header } from 'semantic-ui-react';
import {setBackgroundPicture} from "../../actions/commonActions";
import {reduxActionBinder} from "../../actions/index";
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
            loading: false,
            errorMessage: ''
        }
    }
    toggleHomepage = () => {
        this.setState({ loading: true });
        if (this.props.common.homepage === true) {
            document.dispatchEvent(new CustomEvent("SetHompage", {detail: false, bubbles: true}));
            this.setState({ homepage: false });
            this.props.common.homepage = false;
        }
        else {
            document.dispatchEvent(new CustomEvent("SetHompage", {detail: true, bubbles: true}));
            this.setState({ homepage: true });
            this.props.common.homepage = true;
        }
        this.setState({ loading: false });
    };
    toggleBackground = () => {
        this.setState({loading: true});
        if (this.state.background === true) {
            this.props.dispatch(setBackgroundPicture({
                active: false
            })).then(response => {
                this.setState({loading: false, errorMessage: '', background: false});
            }).catch(err => {
                this.setState({loading: false, errorMessage: err});
            });
        }
        else {
            this.props.dispatch(setBackgroundPicture({
                active: true
            })).then(response => {
                this.setState({loading: false, errorMessage: '', background: true});
            }).catch(err => {
                this.setState({loading: false, errorMessage: err});
            });
        }
    };
    render() {
        return (
            <Segment>
                <Header as='h5'>Choose your preferences</Header>
                <div>
                    <Checkbox toggle checked={this.props.common.homepage} onChange={this.toggleHomepage} disabled={this.state.loading} />
                    <span>Ease.space as Homepage</span>
                </div>
                <p>This option allows you to have the Ease.space page when you’ll open a new tab in your browser.</p>
                <div>
                    <Checkbox toggle checked={this.state.background} onChange={this.toggleBackground} disabled={this.state.loading} />
                    <span>Daily background picture</span>
                </div>
                <p>Each day we’ll display a nice background picture, coming from <a href='https://unsplash.com' target='blank'>unsplash.com</a>, behind your Apps.</p>
            </Segment>
        )
    }
}

export default Preferences;