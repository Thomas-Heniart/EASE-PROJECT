import React from 'react';
import { Grid, Image, Icon, Modal, Header, Input } from 'semantic-ui-react';
import { render } from 'react-router-dom';
import {handleSemanticInput} from "../../utils/utils";

class ShowGrid extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            name: ''
        }
    }

    handleInput = handleSemanticInput.bind(this);

    render() {

        // let appsSorted = this.props.apps.filter((item) => {
        //     return item.category.toLowerCase().search(
        //         this.props.categorySelected.toLowerCase()) !== -1;
        // });

        return (
            <Grid columns={4} className="logoCatalog">
                {this.props.apps.map((item, key) =>
                    <Grid.Column key={key} as='a' className="showSegment" onClick={e => console.log('replace')}>
                        <Image src={item.logo}/>
                        <p>{item.name}</p>
                        <Icon name="add square"/>
                    </Grid.Column>
                )}
            </Grid>
        )
    }
}

export default ShowGrid;